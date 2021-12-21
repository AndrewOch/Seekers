package ru.kpfu.itis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.client.Seeker;
import ru.kpfu.itis.protocols.Message;
import ru.kpfu.itis.protocols.MessageType;
import ru.kpfu.itis.socket.ClientSocket;
import ru.kpfu.itis.utils.GameUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Controller implements Initializable {

    public Label username;
    public Label userGenderAndAge;
    public Label userJob;
    public Label userNature;
    public Label userPast;
    public Label userGossip;
    public Label userInventory;
    public Label userDream;

    public Button loot;
    public Button spy;
    public Button readyForVote;

    public Label hint;

    public Button userRGenderAndAge;
    public Button userRJob;
    public Button userRNature;
    public Button userRPast;
    public Button userRGossip;
    public Button userRDream;
    public TextField usernameType;
    public Button loginButton;
    public VBox messages;
    public TextField messageText;
    public Button sendMessage;
    public VBox playersButtons;

    private GameUtils gameUtils;
    private ClientSocket clientSocket;

    private Seeker player;
    private List<Seeker> opponents = new CopyOnWriteArrayList<>();
    private Seeker currentSeeker;

    private boolean readyForRevealSomeOne = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameUtils = new GameUtils();

        loginButton.setOnMouseClicked(event -> {
            String nickname = usernameType.getText();
            loginButton.setDisable(true);
            usernameType.setEditable(false);
            clientSocket = new ClientSocket();
            clientSocket.connect(this, nickname);
            gameUtils.setClientSocket(clientSocket);
        });

        sendMessage.setOnAction(actionEvent -> {
            Message message = new Message();
            message.setType(MessageType.CHAT);
            message.setBody(messageText.getText());
            clientSocket.sendMessage(message);
            messageText.clear();
        });

        loot.setOnAction(event -> {
            if (player.getLootingRemaining() > 0) {
                gameUtils.loot(player);
                userInventory.setText("Инвентарь: " + player.getInventory());
                clientSocket.sendUserDataToServer(player);
            } else {
                hint.setText("Вы больше не можете обыскивать в этом раунде!");
            }
        });

        spy.setOnAction(event -> {
            if (player.getInventory().size() >= 2) {
                gameUtils.spendItems(player);
                userInventory.setText("Инвентарь: " + player.getInventory());
                readyForRevealSomeOne = true;
            } else {
                hint.setText("На это действие требуется потратить 2 предмета!");
            }
        });

        readyForVote.setOnAction(event -> {
            if (player.getRevealedOwnInfo()) {
                player.setReadyForVote(!player.getReadyForVote());
                Message message = new Message();
                message.setType(MessageType.READY_FOR_VOTE);
                try {
                    message.setBody(new ObjectMapper().writeValueAsString(player));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                clientSocket.sendMessage(message);
            } else {
                hint.setText("Вы ещё не раскрывали свою информацию в этом раунде!");
            }
        });

        userRGenderAndAge.setOnAction(event -> revealInfo(0));
        userRJob.setOnAction(event -> revealInfo(1));
        userRNature.setOnAction(event -> revealInfo(2));
        userRPast.setOnAction(event -> revealInfo(3));
        userRGossip.setOnAction(event -> revealInfo(4));
        userRDream.setOnAction(event -> revealInfo(5));
    }

    public void showPlayerInfo() {
        currentSeeker = player;

        if (!player.getRevealedOwnInfo()) {
            showRevealButtons(player);
        } else {
            closeRevealButtons();
        }

        String agePostfix = "лет";
        if (player.getAge() % 10 == 1) {
            agePostfix = "год";
        }

        if (player.getAge() % 10 > 1 && player.getAge() % 10 < 5) {
            agePostfix = "года";
        }

        username.setText(player.getName());
        userGenderAndAge.setText(player.getGender().getTitle() + ", " + player.getAge() + " " + agePostfix);
        userJob.setText("Работа: " + player.getJob().getTitle());
        userNature.setText("Характер: " + player.getNature().getTitle());
        userPast.setText("Прошлое: " + player.getPast().getTitle());
        userGossip.setText("Слухи: " + player.getGossip().getTitle());
        userInventory.setText("Инвентарь: " + player.getInventory());
        userDream.setText("Мечта: " + player.getDream().getTitle());
    }

    private void revealInfo(Integer infoId) {
        if (currentSeeker.equals(this)) {
            revealOwnInfo(infoId);
        } else {
            revealOpponentInfo(infoId);
        }
    }

    private void revealOwnInfo(Integer infoId) {
        player.getRevealed()[infoId] = true;
        player.setRevealedOwnInfo(true);

        Message message = new Message();
        message.setType(MessageType.REVEAL_SELF);
        try {
            message.setBody(new ObjectMapper().writeValueAsString(player));
            message.addHeader("revealed", getRevealedInfoAsString(player, infoId));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        clientSocket.sendMessage(message);
    }

    private void revealOpponentInfo(Integer infoId) {
        currentSeeker.getRevealed()[infoId] = true;
        showOpponentInfo();

        Message message = new Message();
        message.setType(MessageType.REVEAL);
        try {
            message.setBody(new ObjectMapper().writeValueAsString(currentSeeker));
            message.addHeader("revealed", getRevealedInfoAsString(currentSeeker, infoId));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        clientSocket.sendMessage(message);
    }

    private String getRevealedInfoAsString(Seeker seeker, Integer infoId) {
        String revealedInfo = "";
        switch (infoId) {
            case 0 -> {
                Integer age = seeker.getAge();
                String agePostfix = "лет";
                if (age % 10 == 1) {
                    agePostfix = "год";
                }
                if (age % 10 > 1 && age % 10 < 5) {
                    agePostfix = "года";
                }
                revealedInfo = seeker.getGender().getTitle() + ", " + age + " " + agePostfix;
            }
            case 1 -> revealedInfo = seeker.getJob().getTitle();
            case 2 -> revealedInfo = seeker.getNature().getTitle();
            case 3 -> revealedInfo = seeker.getPast().getTitle();
            case 4 -> revealedInfo = seeker.getGossip().getTitle();
            case 5 -> revealedInfo = seeker.getDream().getTitle();
        }
        return revealedInfo;
    }

    public void showOpponentInfo(UUID uuid) {
        currentSeeker = opponents.stream().filter(op -> op.getUuid() == uuid).findFirst().get();
        showOpponentInfo();
    }

    public void showOpponentInfo() {
        username.setText(currentSeeker.getName());

        if (readyForRevealSomeOne) {
            showRevealButtons(currentSeeker);
        } else {
            closeRevealButtons();
        }

        boolean[] revealed = currentSeeker.getRevealed();

        if (revealed[0]) {
            Integer age = currentSeeker.getAge();

            String agePostfix = "лет";
            if (age % 10 == 1) {
                agePostfix = "год";
            }

            if (age % 10 > 1 && age % 10 < 5) {
                agePostfix = "года";
            }

            userGenderAndAge.setText(currentSeeker.getGender().getTitle() + ", " + age + " " + agePostfix);
        } else {
            userGenderAndAge.setText("??? ???");
        }

        if (revealed[1]) {
            userJob.setText("Работа: " + currentSeeker.getJob().getTitle());
        } else {
            userJob.setText("Работа: ???");
        }

        if (revealed[2]) {
            userNature.setText("Характер: " + currentSeeker.getNature().getTitle());

        } else {
            userNature.setText("Характер: ???");
        }

        if (revealed[3]) {
            userPast.setText("Прошлое: " + currentSeeker.getPast().getTitle());

        } else {
            userPast.setText("Прошлое: ???");
        }

        if (revealed[4]) {
            userGossip.setText("Слухи: " + currentSeeker.getGossip().getTitle());
        } else {
            userGossip.setText("Слухи: ???");
        }

        if (revealed[5]) {
            userDream.setText("Мечта: " + currentSeeker.getDream().getTitle());
        } else {
            userDream.setText("Мечта: ???");
        }

        userInventory.setText("Инвентарь: " + currentSeeker.getInventory());
        readyForRevealSomeOne = false;
    }

    public Seeker getPlayer() {
        return player;
    }

    public void setPlayer(Seeker player) {
        this.player = player;
    }

    public List<Seeker> getOpponents() {
        return opponents;
    }

    public void showRevealButtons(Seeker seeker) {

        if (!seeker.getRevealed()[0]) userRGenderAndAge.setVisible(true);
        if (!seeker.getRevealed()[1]) userRJob.setVisible(true);
        if (!seeker.getRevealed()[2]) userRNature.setVisible(true);
        if (!seeker.getRevealed()[3]) userRPast.setVisible(true);
        if (!seeker.getRevealed()[4]) userRGossip.setVisible(true);
        if (!seeker.getRevealed()[5]) userRDream.setVisible(true);
    }

    public void closeRevealButtons() {

        userRGenderAndAge.setVisible(false);
        userRJob.setVisible(false);
        userRNature.setVisible(false);
        userRPast.setVisible(false);
        userRDream.setVisible(false);
        userRGossip.setVisible(false);
    }

}


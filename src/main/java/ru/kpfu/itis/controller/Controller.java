package ru.kpfu.itis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.client.Seeker;
import ru.kpfu.itis.enums.gameParameters.Item;
import ru.kpfu.itis.protocols.Message;
import ru.kpfu.itis.protocols.MessageType;
import ru.kpfu.itis.socket.ClientSocket;
import ru.kpfu.itis.utils.GameUtils;

import java.net.URL;
import java.util.List;
import java.util.Random;
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

    public Label opponentUsername;
    public Label opponentGenderAndAge;
    public Label opponentJob;
    public Label opponentNature;
    public Label opponentPast;
    public Label opponentGossip;
    public Label opponentInventory;
    public Label opponentDream;

    public Button loot;
    public Button spy;
    public Button readyForVote;

    public Label hint;

    public VBox opponentInfo;
    public VBox revealPanel;

    public Button revealGenderAndAge;
    public Button revealJob;
    public Button revealNature;
    public Button revealPast;
    public Button revealGossip;
    public Button revealDream;

    public VBox userRevealPanel;
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
    private Seeker currentOpponent;

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

                revealPanel.setVisible(true);
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

        revealGenderAndAge.setOnAction(event -> revealOpponentInfo(0));
        revealJob.setOnAction(event -> revealOpponentInfo(1));
        revealNature.setOnAction(event -> revealOpponentInfo(2));
        revealPast.setOnAction(event -> revealOpponentInfo(3));
        revealGossip.setOnAction(event -> revealOpponentInfo(4));
        revealDream.setOnAction(event -> revealOpponentInfo(5));

        userRGenderAndAge.setOnAction(event -> revealOwnInfo(0));
        userRJob.setOnAction(event -> revealOwnInfo(1));
        userRNature.setOnAction(event -> revealOwnInfo(2));
        userRPast.setOnAction(event -> revealOwnInfo(3));
        userRGossip.setOnAction(event -> revealOwnInfo(4));
        userRDream.setOnAction(event -> revealOwnInfo(5));
    }

    public void showPlayerInfo() {

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


    private void revealOwnInfo(Integer infoId) {
        player.getRevealed()[infoId] = true;
        userRevealPanel.setVisible(false);
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
        currentOpponent.getRevealed()[infoId] = true;
        showOpponentInfo();
        revealPanel.setVisible(false);
        opponentInfo.setVisible(true);

        Message message = new Message();
        message.setType(MessageType.REVEAL);
        try {
            message.setBody(new ObjectMapper().writeValueAsString(currentOpponent));
            message.addHeader("revealed", getRevealedInfoAsString(currentOpponent, infoId));
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
        currentOpponent = opponents.stream().filter(op -> op.getUuid() == uuid).findFirst().get();
        showOpponentInfo();
    }

    public void showOpponentInfo() {
        opponentUsername.setText(currentOpponent.getName());

        boolean[] revealed = currentOpponent.getRevealed();

        if (revealed[0]) {
            Integer age = currentOpponent.getAge();

            String agePostfix = "лет";
            if (age % 10 == 1) {
                agePostfix = "год";
            }

            if (age % 10 > 1 && age % 10 < 5) {
                agePostfix = "года";
            }

            opponentGenderAndAge.setText(currentOpponent.getGender().getTitle() + ", " + age + " " + agePostfix);
            revealGenderAndAge.setVisible(false);
        } else {
            opponentGenderAndAge.setText("??? ???");
            revealGenderAndAge.setVisible(true);
        }

        if (revealed[1]) {
            opponentJob.setText("Работа: " + currentOpponent.getJob().getTitle());
            revealJob.setVisible(false);

        } else {
            opponentJob.setText("Работа: ???");
            revealJob.setVisible(true);

        }

        if (revealed[2]) {
            opponentNature.setText("Характер: " + currentOpponent.getNature().getTitle());
            revealNature.setVisible(false);

        } else {
            opponentNature.setText("Характер: ???");
            revealNature.setVisible(true);

        }

        if (revealed[3]) {
            opponentPast.setText("Прошлое: " + currentOpponent.getPast().getTitle());
            revealPast.setVisible(false);

        } else {
            opponentPast.setText("Прошлое: ???");
            revealPast.setVisible(true);

        }

        if (revealed[4]) {
            opponentGossip.setText("Слухи: " + currentOpponent.getGossip().getTitle());
            revealGossip.setVisible(false);
        } else {
            opponentGossip.setText("Слухи: ???");
            revealGossip.setVisible(true);
        }

        if (revealed[5]) {
            opponentDream.setText("Мечта: " + currentOpponent.getDream().getTitle());
            revealDream.setVisible(false);
        } else {
            opponentDream.setText("Мечта: ???");
            revealDream.setVisible(true);
        }

        opponentInventory.setText("Инвентарь: " + currentOpponent.getInventory());
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

}


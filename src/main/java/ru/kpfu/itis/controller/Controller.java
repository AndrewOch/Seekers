package ru.kpfu.itis.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.client.Seeker;
import ru.kpfu.itis.socket.ClientSocket;
import ru.kpfu.itis.utils.GameUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

    public Button opponent1;
    public Button opponent2;
    public Button opponent3;
    public Button opponent4;
    public Button opponent5;
    public Button opponent6;
    public Button opponent7;

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

    private GameUtils gameUtils;
    private ClientSocket clientSocket;

    private List<Seeker> opponents;
    private Seeker currentOpponent;
    private Seeker player;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameUtils = new GameUtils();
        clientSocket = new ClientSocket();
        clientSocket.connect();

        player = new Seeker("Fernando");

        opponents = new ArrayList<>();
        opponents.add(new Seeker("Ann"));
        opponents.add(new Seeker("Tim"));
        opponents.add(new Seeker("Edna"));

        opponent1.setText(opponents.get(0).getName());
        opponent2.setText(opponents.get(1).getName());
        opponent3.setText(opponents.get(2).getName());
        opponent4.setVisible(false);
        opponent5.setVisible(false);
        opponent6.setVisible(false);
        opponent7.setVisible(false);

        Integer age = player.getAge();

        String agePostfix = "лет";
        if (age % 10 == 1) {
            agePostfix = "год";
        }

        if (age % 10 > 1 && age % 10 < 5) {
            agePostfix = "года";
        }

        username.setText(player.getName());
        userGenderAndAge.setText(player.getGender().getTitle() + ", " + age + " " + agePostfix);
        userJob.setText("Работа: " + player.getJob().getTitle());
        userNature.setText("Характер: " + player.getNature().getTitle());
        userPast.setText("Прошлое: " + player.getPast().getTitle());
        userGossip.setText("Слухи: " + player.getGossip().getTitle());
        userInventory.setText("Инвентарь: " + player.getInventory());
        userDream.setText("Мечта: " + player.getDream().getTitle());

        showOpponentInfo(0);

        loot.setOnAction(event -> {
            if (player.getLootingRemaining() > 0) {
                gameUtils.loot(player);
                userInventory.setText("Инвентарь: " + player.getInventory());
            } else {
                hint.setText("Вы больше не можете обыскивать в этом раунде!");
            }
        });

        spy.setOnAction(event -> {
            if (player.getInventory().size() >= 2) {
                gameUtils.spendItems(player);
                userInventory.setText("Инвентарь: " + player.getInventory());


                //opponentInfo.setVisible(false);
                revealPanel.setVisible(true);
            } else {
                hint.setText("На это действие требуется потратить 2 предмета!");
            }
        });

        readyForVote.setOnAction(event -> {
            if (player.getRevealedOwnInfo()) {
                player.setReadyForVote(!player.getReadyForVote());
            } else {
                hint.setText("Вы ещё не раскрывали свою информацию в этом раунде!");
            }
        });

        opponent1.setOnAction(event -> showOpponentInfo(0));
        opponent2.setOnAction(event -> showOpponentInfo(1));
        opponent3.setOnAction(event -> showOpponentInfo(2));
        opponent4.setOnAction(event -> showOpponentInfo(3));
        opponent5.setOnAction(event -> showOpponentInfo(4));
        opponent6.setOnAction(event -> showOpponentInfo(5));
        opponent7.setOnAction(event -> showOpponentInfo(6));

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


    private void revealOwnInfo(Integer infoId) {
        player.getRevealed()[infoId] = true;
        userRevealPanel.setVisible(false);
        player.setRevealedOwnInfo(true);
    }

    private void revealOpponentInfo(Integer infoId) {
        currentOpponent.getRevealed()[infoId] = true;
        showOpponentInfo();
        revealPanel.setVisible(false);
        opponentInfo.setVisible(true);
    }

    private void showOpponentInfo(Integer id) {
        currentOpponent = opponents.get(id);
        showOpponentInfo();
    }

    private void showOpponentInfo() {
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
}


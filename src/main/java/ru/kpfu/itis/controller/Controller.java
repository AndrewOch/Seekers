package ru.kpfu.itis.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import ru.kpfu.itis.client.Seeker;
import ru.kpfu.itis.socket.ClientSocket;
import ru.kpfu.itis.utils.GameUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    public Label username;
    public Label userGenderAndAge;
    public Label userJob;
    public Label userNature;
    public Label userPast;
    public Label userGossip;
    public Label userInventory;
    public Label userDream;
    private GameUtils gameUtils;
    private ClientSocket clientSocket;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameUtils = new GameUtils();
        clientSocket = new ClientSocket();
        //clientSocket.connect();

        Seeker seeker = new Seeker("Fernando");
        Integer age = seeker.getAge();

        String agePostfix = "лет";
        if (age % 10 == 1) {
            agePostfix = "год";
        }

        if (age % 10 > 1 && age % 10 < 5) {
            agePostfix = "года";
        }

        username.setText(seeker.getName());
        userGenderAndAge.setText(seeker.getGender().getTitle() + ", " + age + " " + agePostfix);
        userJob.setText("Работа: " + seeker.getJob().getTitle());
        userNature.setText("Характер: " + seeker.getNature().getTitle());
        userPast.setText("Прошлое: " + seeker.getPast().getTitle());
        userGossip.setText("Слухи: " + seeker.getGossip().getTitle());
        userInventory.setText("Инвентарь: " + seeker.getInventory());
        userDream.setText("Мечта: " + seeker.getDream().getTitle());


//        try {
//            Image playerIcon = new Image(new FileInputStream(PLAYER_ICON));
//
//            player.setImage(playerIcon);
//            player.setRotate(180);
//
//            enemy.setImage(playerIcon);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        helloButton.setOnAction(event -> {
//            helloLabel.setText("Привет, игрок!");
//        });
    }
}

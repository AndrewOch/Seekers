package ru.kpfu.itis.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import ru.kpfu.itis.enums.Action;
import ru.kpfu.itis.socket.ClientSocket;
import ru.kpfu.itis.utils.GameUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private GameUtils gameUtils;

    private ClientSocket clientSocket;

    @FXML
    private Button helloButton;

    @FXML
    private Label helloLabel;

    @FXML
    private ImageView player;

    @FXML
    private ImageView enemy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameUtils = new GameUtils();
        clientSocket = new ClientSocket();
        clientSocket.connect();
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
        helloButton.setOnAction(event -> {
            helloLabel.setText("Привет, игрок!");
        });
    }

    private final EventHandler<KeyEvent> playerControlEvent = event -> {
        switch (event.getCode()) {
            case RIGHT: {
                //gameUtils.goRight(player);
                clientSocket.sendMessage(Action.SAY.getCode());
                System.out.println(Action.SAY.getDescription());
                break;
            }
            case LEFT: {
                gameUtils.goOut(player);
                clientSocket.sendMessage(Action.GO_OUT.getCode());
                System.out.println(Action.GO_OUT.getDescription());
                break;
            }
            case SPACE: {
                gameUtils.spy(player);
                clientSocket.sendMessage(Action.GO_OUT.getCode());
                System.out.println(Action.GO_OUT.getDescription());
            }
            case ESCAPE: {
                //TODO выход из игры
            }

        }
    };

    public EventHandler<KeyEvent> getPlayerControlEvent() {
        return playerControlEvent;
    }
}
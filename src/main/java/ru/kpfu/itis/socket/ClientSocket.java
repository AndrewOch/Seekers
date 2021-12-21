package ru.kpfu.itis.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import ru.kpfu.itis.client.Seeker;
import ru.kpfu.itis.controller.Controller;
import ru.kpfu.itis.protocols.Message;
import ru.kpfu.itis.protocols.MessageType;
import ru.kpfu.itis.server.GameServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class ClientSocket extends Thread {

    private Socket clientSocket;

    private GameServer chatServer;

    private final String HOST = "localhost";
    private final int PORT = 5050;

    private PrintWriter out;
    private BufferedReader fromServer;

    private Controller controller;

    public void connect(Controller controller, String nickname) {
        try {
            this.controller = controller;
            clientSocket = new Socket(HOST, PORT);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            chatServer = GameServer.getInstance();

            Message message = new Message();
            message.setType(MessageType.CONNECT);
            message.addHeader("nickname", nickname);
            sendMessage(message);
            this.start();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void sendMessage(Message message) {
        try {
            String jsonMessage = new ObjectMapper().writeValueAsString(message);
            System.out.println(jsonMessage);
            out.println(jsonMessage);
        } catch (JsonProcessingException e) {
            //console log
        }
    }

    @Override
    public void run() {
        while (true) {
            String messageFromServer;
            Message message = null;
            try {
                messageFromServer = fromServer.readLine();
                System.out.println(messageFromServer);
                message = new ObjectMapper().readValue(messageFromServer, Message.class);
                System.out.println(message);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            switch (message.getType()) {
                case CHAT: {
                    Label label = new Label();
                    label.setText(message.getBody());
                    label.setFont(Font.font("Arial"));
                    Platform.runLater(() -> controller.messages.getChildren().add(label));
                    break;
                }
                case CREATE_CHARACTER: {

                    try {
                        Seeker seeker = new ObjectMapper().readValue(message.getBody(), Seeker.class);
                        controller.setPlayer(seeker);

                        Platform.runLater(() -> controller.showPlayerInfo());

                        Button button = new Button();
                        button.setText(seeker.getName());
                        button.setOnAction(actionEvent -> controller.showPlayerInfo());
                        Platform.runLater(() -> controller.playersButtons.getChildren().add(button));

                        Platform.runLater(() -> controller.messageText.setDisable(false));
                        Platform.runLater(() -> controller.sendMessage.setDisable(false));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case CONNECT_TO_OTHER_PLAYER: {

                    try {
                        Seeker seeker = new ObjectMapper().readValue(message.getBody(), Seeker.class);

                        controller.getOpponents().add(seeker);

                        Button button = new Button();
                        button.setText(seeker.getName());
                        button.setOnAction(actionEvent -> controller.showOpponentInfo(seeker.getUuid()));
                        Platform.runLater(() -> controller.playersButtons.getChildren().add(button));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case READY_FOR_START: {
                    controller.loot.setVisible(true);
                    controller.spy.setVisible(true);
                    controller.readyForVote.setVisible(true);
                    break;
                }
                case UPDATE_INFO: {
                    try {
                        Seeker seeker = new ObjectMapper().readValue(message.getBody(), Seeker.class);
                        if (controller.getPlayer().getUuid().equals(seeker.getUuid())) {
                            controller.setPlayer(seeker);
                        } else {
                            for (Seeker seeker1 : controller.getOpponents()) {
                                if (seeker1.getUuid().equals(seeker.getUuid())) {
                                    copySeekerModel(seeker, seeker1);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private void copySeekerModel(Seeker from, Seeker to) {
        to.setName(from.getName());
        to.setGender(from.getGender());
        to.setAge(from.getAge());
        to.setJob(from.getJob());
        to.setNature(from.getNature());
        to.setPast(from.getPast());
        to.setGossip(from.getGossip());
        to.setInventory(from.getInventory());

        to.setLootingRemaining(from.getLootingRemaining());
        to.setRevealedOwnInfo(from.getRevealedOwnInfo());
        to.setRevealed(from.getRevealed());
        to.setReadyForVote(from.getReadyForVote());
    }

    public void sendUserDataToServer(Seeker seeker) {
        try {
            String json = new ObjectMapper().writeValueAsString(seeker);
            Message message = new Message();
            message.setType(MessageType.UPDATE_INFO);
            message.setBody(json);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
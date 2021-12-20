package ru.kpfu.itis.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.protocols.Message;
import ru.kpfu.itis.protocols.MessageType;
import ru.kpfu.itis.server.GameServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

public class Client extends Thread {

    private UUID uuid;
    private String nickname;

    private Seeker seeker;

    private Socket client;

    // поток символов, которые мы отправляем клиенту
    private PrintWriter toClient;

    // поток символов, которые мы читаем от клиента
    private BufferedReader fromClient;

    private GameServer server;

    public Client(Socket client) {
        server = GameServer.getInstance();
        this.client = client;
        try {
            // обернули потоки байтов в потоки символов
            this.toClient = new PrintWriter(client.getOutputStream(), true);
            this.fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // мы в любое время можем получить сообщение от клиента
    // поэтому чтение сообщения от клиента должно происходить в побочном потоке
    @Override
    public void run() {
        while (true) {
            // прочитали сообщение от клиента
            String messageFromClient;

            try {
                messageFromClient = fromClient.readLine();
                if (messageFromClient != null) {
                    System.out.println(nickname + ": " + messageFromClient);

                    Message message = new ObjectMapper().readValue(messageFromClient, Message.class);

                    List<Client> clients = server.getClients();

                    switch (message.getType()) {
                        case CONNECT: {
                            this.nickname = message.getHeaders().get("nickname");
                            this.seeker = new Seeker(nickname);
                            Message createCharacterMessage = new Message();
                            createCharacterMessage.setType(MessageType.CREATE_CHARACTER);
                            createCharacterMessage.addHeader("id", String.valueOf(server.getClients().indexOf(this)));
                            createCharacterMessage.setBody(new ObjectMapper().writeValueAsString(seeker));
                            sendMessage(createCharacterMessage);

                            for (Message m : server.getChatMessages()) {
                                this.sendMessage(m);
                            }

                            Message alarm = new Message();
                            alarm.setType(MessageType.CHAT);
                            if (server.readyToPlay()) {
                                alarm.setBody(nickname + " вошёл в игру. Игра начинается!");
                                server.rememberChatMessage(alarm);

                                for (Client client : clients) {
                                    client.sendMessage(alarm);
                                }
                                alarm.setBody("Добро пожаловать в башню мага! Волшебник исполнит любую вашу мечту, " +
                                        "но только двоим из вас. Решите, кто это будет. Пусть все раскроют правду о себе" +
                                        " и затем вы проголосуете против одного из вас.");
                                Message start = new Message();
                                start.setType(MessageType.READY_FOR_START);
                                for (Client client : clients) {
                                    client.sendMessage(alarm);
                                }
                            } else {
                                alarm.setBody(nickname + " вошёл в игру. Для начала требуется ещё игроков: " + server.playersRemainingToPlay());
                            }

                            server.rememberChatMessage(alarm);

                            for (Client client : clients) {
                                client.sendMessage(alarm);
                            }

                            Message connectToOthers = new Message();
                            connectToOthers.setType(MessageType.CONNECT_TO_OTHER_PLAYER);
                            connectToOthers.addHeader("id", String.valueOf(server.getClients().indexOf(this)));
                            connectToOthers.setBody(new ObjectMapper().writeValueAsString(seeker));

                            for (Client client : clients) {
                                if (client != this) {
                                    client.sendMessage(connectToOthers);

                                    Message connectTo = new Message();
                                    connectTo.setType(MessageType.CONNECT_TO_OTHER_PLAYER);
                                    connectTo.addHeader("id", String.valueOf(server.getClients().indexOf(this)));
                                    connectTo.setBody(new ObjectMapper().writeValueAsString(client.seeker));
                                    this.sendMessage(connectTo);
                                }
                            }

                            System.out.println(createCharacterMessage);
                            break;
                        }
                        case CHAT: {
                            message.setBody(nickname + ": " + message.getBody());
                            server.rememberChatMessage(message);

                            for (Client client : clients) {
                                System.out.println(client.nickname);
                                client.sendMessage(message);
                            }

                            break;
                        }
                    }

                }
            } catch (IOException e) {
                //
            }
        }
    }

    public void sendMessage(Message message) {
        try {
            String jsonMessage = new ObjectMapper().writeValueAsString(message);
            System.out.println("SEND message TO: " + nickname + " " + jsonMessage);
            toClient.println(jsonMessage);
        } catch (JsonProcessingException e) {
            //console log
        }
    }

    public Socket getClient() {
        return client;
    }

    public String getNickname() {
        return nickname;
    }

    public Seeker getSeeker() {
        return seeker;
    }

    public void setSeeker(Seeker seeker) {
        this.seeker = seeker;
    }
}
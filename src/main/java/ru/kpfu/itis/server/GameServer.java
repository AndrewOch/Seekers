package ru.kpfu.itis.server;

import ru.kpfu.itis.client.Client;
import ru.kpfu.itis.protocols.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameServer {

    private ServerSocket serverSocket;

    private Integer playersNeeded = 4;

    private List<Client> clients = new CopyOnWriteArrayList<>();

    private static GameServer chatServer;

    private List<Message> chatMessages = new CopyOnWriteArrayList<>();

    public static GameServer getInstance() {
        if (chatServer == null) {
            chatServer = new GameServer();
        }
        return chatServer;
    }

    private GameServer() {
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(5050);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Socket socket = serverSocket.accept();
                            Client client = new Client(socket);
                            clients.add(client);
                            client.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            //console log
        }
    }

    public List<Client> getClients() {
        return clients;
    }

    public Client getClient(Socket socket) {
        System.out.println(clients);
        for (Client client : clients) {
            if (socket.equals(client.getClient())) {
                return client;
            }
        }
        return null;
    }

    public List<Message> getChatMessages() {
        return chatMessages;
    }

    public void rememberChatMessage(Message message) {
        if (chatMessages.size() < 100) {
            chatMessages.add(message);
        } else {
            chatMessages.remove(0);
            chatMessages.add(message);
        }
    }

    public boolean readyToPlay() {
        return (playersNeeded <= clients.size());
    }

    public int playersRemainingToPlay() {
        if (readyToPlay()) {
            return 0;
        } else {
            return playersNeeded - clients.size();
        }
    }

    public Integer getPlayersNeeded() {
        return playersNeeded;
    }

    public void setPlayersNeeded(Integer playersNeeded) {
        this.playersNeeded = playersNeeded;
    }
}
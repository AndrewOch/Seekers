package ru.kpfu.itis.server;

public class ServerApp {

    public static void main(String[] args) {
        GameServer chatServer = GameServer.getInstance();
        chatServer.start();
    }
}
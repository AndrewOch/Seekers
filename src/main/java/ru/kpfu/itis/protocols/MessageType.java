package ru.kpfu.itis.protocols;

public enum MessageType {
    CONNECT("connect", "Подключение к серверу"),
    CREATE_CHARACTER("create character", "generate player's character"),
    CHAT("chat", "Сообщение другим пользователям"),
    READY_FOR_VOTE("ready for vote", "ready for vote"),
    READY_FOR_START("ready for start", "ready for start"),
    REVEAL("reveal info", "reveal someones info"),
    REVEAL_SELF("reveal self info", "reveal self info"),
    LOOT("loot", "loot the tower and find items"),
    KICKED("kicked from match", "that player is kicked form match"),
    CONNECT_TO_OTHER_PLAYER("connect to other player", "used to register other player view while he is connecting"),
    UPDATE_INFO("update player info", "update player info");

    private final String title;
    private final String description;

    MessageType(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "MessageType{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
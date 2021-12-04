package main.java.ru.kpfu.itis.enums;

public enum Action {

    SAY(0, "Say", "Player send a message into chat"),
    GO_OUT(1, "Go out", "Player goes out from a bunker to find some things"),
    SPY(2, "Spy", "Player spies on other player to reveal some secrets");

    private final int code;
    private final String title;
    private final String description;

    Action(int code, String title, String description) {
        this.code = code;
        this.title = title;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
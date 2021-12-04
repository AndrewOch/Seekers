package main.java.ru.kpfu.itis.enums.gameParameters;

import java.util.Random;

public enum Extra {

    NO_INFO("нет информации"),
    LEFT_HANDED("левша"),
    MASTER("мастер на все руки");

    private final String title;

    Extra(String title) {
        this.title = title;
    }

    public static Extra random() {
        int pick = new Random().nextInt(Extra.values().length);
        return Extra.values()[pick];
    }

    public String getTitle() {
        return title;
    }
}
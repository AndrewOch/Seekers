package main.java.ru.kpfu.itis.enums.gameParameters;

import java.util.Random;

public enum Health {

    PERFECT("идеальное здоровье"),
    CANCER("рак"),
    BROKE_ARM("перелом руки"),
    BROKE_LEG("перелом ноги"),
    BROKE_FINGER("перелом пальца"),
    GOUT("подагра"),
    NO_ILL("не болеет"),
    BLACK_DEATH("чума"),
    SYPHILIS("сифилис"),
    RUNNY_NOSE("насморк"),
    IMMORTAL("бессмертие");

    private final String title;

    Health(String title) {
        this.title = title;
    }

    public static Health random() {
        int pick = new Random().nextInt(Health.values().length);
        return Health.values()[pick];
    }

    public String getTitle() {
        return title;
    }
}
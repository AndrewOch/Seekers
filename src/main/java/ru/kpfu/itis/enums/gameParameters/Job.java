package main.java.ru.kpfu.itis.enums.gameParameters;

import java.util.Random;

public enum Job {

    HOMELESS("бродяга"),
    KNIGHT("рыцарь"),
    NOMAD("кочевник"),
    PEASANT("крестьянин"),
    HUNTER("охотник"),
    SEWING("шитьё");

    private final String title;

    Job(String title) {
        this.title = title;
    }

    public static Job random() {
        int pick = new Random().nextInt(Job.values().length);
        return Job.values()[pick];
    }

    public String getTitle() {
        return title;
    }
}
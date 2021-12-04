package enums;

import java.util.Random;

public enum Phobia {

    NO_PHOBIA("нет"),
    ARACHNOPHOBIA("арахнофобия (боязнь пауков)");

    private String title;

    Phobia(String title) {
        this.title = title;
    }

    public static Phobia random() {
        int pick = new Random().nextInt(Phobia.values().length);
        return Phobia.values()[pick];
    }

    public String getTitle() {
        return title;
    }
}
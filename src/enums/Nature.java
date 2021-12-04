package enums;

import java.util.Random;

public enum Nature {

    CALM("спокойный"),
    PASSIONATE("вспыльчивый");

    private String title;

    Nature(String title) {
        this.title = title;
    }

    public static Nature random() {
        int pick = new Random().nextInt(Nature.values().length);
        return Nature.values()[pick];
    }

    public String getTitle() {
        return title;
    }
}
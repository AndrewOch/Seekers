package enums;

import java.util.Random;

public enum Extra {

    SIMPLE("простак"),
    LEFT_HANDED("левша"),
    MASTER("мастер на все руки");

    private String title;

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
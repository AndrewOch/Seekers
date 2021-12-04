package enums;

import java.util.Random;

public enum Hobby {

    NO_HOBBY("нет"),
    SEWING("шитьё");

    private final String title;

    Hobby(String title) {
        this.title = title;
    }

    public static Hobby random() {
        int pick = new Random().nextInt(Hobby.values().length);
        return Hobby.values()[pick];
    }

    public String getTitle() {
        return title;
    }
}
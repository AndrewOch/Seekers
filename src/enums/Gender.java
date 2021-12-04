package enums;

import java.util.Random;

public enum Gender {

    MALE("мужчина"),
    FEMALE("женщина");

    private String title;

    Gender(String title) {
        this.title = title;
    }

    public static Gender random() {
        int pick = new Random().nextInt(Gender.values().length);
        return Gender.values()[pick];
    }

    public String getTitle() {
        return title;
    }
}
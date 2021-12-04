package enums;

import java.util.Random;

public enum Health {

    PERFECT("идеальное здоровье"),
    CANCER_LUNGS("рак лёгких"),
    CANCER_BRAIN("рак мозга"),
    CANCER_LIVER("рак печени"),
    BROKE_ARM("перелом руки"),
    BROKE_LEG("перелом ноги"),
    BROKE_FINGER("перелом пальца"),
    GOUT("подагра"),
    NO_ILL("не болеет"),
    RUNNY_NOSE("насморк"),
    AIDS("СПИД"),
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
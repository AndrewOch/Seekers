package main.java.ru.kpfu.itis.enums.gameParameters;

import java.util.Random;

public enum Dream {

    BECOME_IMMORTAL(0, "стать бессмертным"),
    FIND_LOVE(1, "найти любовь"),
    BECOME_RICH(2, "стать богатым"),
    OVERCOME_PHOBIAS(3, "перебороть страхи"),
    CHANGE_GENDER(4, "сменить пол");

    private final Integer id;
    private final String title;

    Dream(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Dream random() {
        int pick = new Random().nextInt(Dream.values().length);
        return Dream.values()[pick];
    }

    public String getTitle() {
        return title;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return title;
    }
}
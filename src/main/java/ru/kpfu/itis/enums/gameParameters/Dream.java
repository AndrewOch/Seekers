package ru.kpfu.itis.enums.gameParameters;

import java.util.Random;

public enum Dream {

    BECOME_IMMORTAL(0, "стать бессмертным"),
    FIND_LOVE(1, "найти любовь"),
    BECOME_RICH(2, "стать богатым"),
    OVERCOME_PHOBIAS(3, "перебороть страхи"),
    BECOME_POPULAR(4, "стать знаменитым"),
    CHANGE_GENDER(5, "сменить пол"),
    KILL_THE_WIZARD(6, "убить волшебника"),
    SUMMON_DEMON(7, "призвать демона"),
    BECOME_BEAUTIFUL(8, "стать безмерно красивым"),
    LIVE_IN_OWN_CASTLE(9, "жить в собственном замке"),
    BECOME_A_WIZARD(10, "стать волшебником");

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
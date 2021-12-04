package ru.kpfu.itis.enums.gameParameters;

import java.util.Random;

public enum Past {

    NO_INFO("неизвестно"),
    BORN_ON_FARM("родом с фермы"),
    PARENTS_DIED_EARLY("родители рано погибли"),
    KILLED_A_PERSON("убил(а) человека"),
    STOLE_MONEY("крал(а) деньги"),
    GREW_CROPS("выращивал(а) посевы"),
    BUILT_HOUSE("строил(а) дома"),
    BEEN_BETRAYED("стал(а) жертвой предательства"),
    SOLD_DRUGS("продавал(а) наркотики"),
    SOLD_WEAPONS("продавал(а) оружие"),
    FROM_NOBLE_FAMILY("из знатной семьи");

    private final String title;

    Past(String title) {
        this.title = title;
    }

    public static Past random() {
        int pick = new Random().nextInt(Past.values().length);
        return Past.values()[pick];
    }

    public String getTitle() {
        return title;
    }
}
package ru.kpfu.itis.enums.gameParameters;

import java.util.Random;

public enum Gossip {

    NO_INFO("нет информации"),
    LEFT_HANDED("левша"),
    GOOD_REPUTATION("хорошая репутация"),
    STRANGE("странный(ая)"),
    RELIABLE("надежный(ая)"),
    SUSPICIOUS("подозрительный(ая)"),
    KNOWS_BEER("знает толк в пиве"),
    KNOWS_MEDICINE("знает толк в медицине"),
    MASTER("мастер на все руки");

    private final String title;

    Gossip(String title) {
        this.title = title;
    }

    public static Gossip random() {
        int pick = new Random().nextInt(Gossip.values().length);
        return Gossip.values()[pick];
    }

    public String getTitle() {
        return title;
    }
}
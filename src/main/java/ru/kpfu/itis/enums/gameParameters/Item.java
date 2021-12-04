package main.java.ru.kpfu.itis.enums.gameParameters;

import java.util.Random;

public enum Item {

    STICK(0, "палка"),
    KNIFE(1, "нож"),
    BREAD(2, "хлеб"),
    HEALING_HERBS(3, "целебные травы"),
    SOCKS(4, "лапти"),
    BASKET(5, "корзина"),
    APPLE(6, "яблоко");

    private final Integer id;
    private final String title;

    Item(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Item random() {
        int pick = new Random().nextInt(Item.values().length);
        return Item.values()[pick];
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
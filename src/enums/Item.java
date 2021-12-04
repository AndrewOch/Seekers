package enums;

import java.util.Random;

public enum Item {

    GUN(0, "пистолет"),
    KNIFE(1, "нож"),
    BREAD(2, "хлеб"),
    DRUGS(3, "лекарства"),
    SOCKS(4, "носки"),
    PHONE(5, "телефон"),
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
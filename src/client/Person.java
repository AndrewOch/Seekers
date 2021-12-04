package client;

import enums.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Person {

    private String name;
    private Gender gender;
    private Integer age;
    private Health health;
    private Hobby hobby;
    private Nature nature;
    private Phobia phobia;
    private Extra extra;
    private List<Item> inventory;

    public Person(String username) {
        this.name = username;
        this.gender = Gender.random();
        this.age = new Random().nextInt(80) + 18;
        this.health = Health.random();
        this.hobby = Hobby.random();
        this.nature = Nature.random();
        this.phobia = Phobia.random();
        this.extra = Extra.random();

        this.inventory = new ArrayList<>();
        this.inventory.add(Item.random());
    }

    @Override
    public String toString() {
        String agePostfix = "лет";
        if (age % 10 == 1) {
            agePostfix = "год";
        }

        if (age % 10 > 1 && age % 10 < 5) {
            agePostfix = "года";
        }

        return name + ":\n" +
                gender.getTitle() + ", " + age + " " + agePostfix + "\n" +
                "Здоровье: " + health.getTitle() + '\n' +
                "Хобби: " + hobby.getTitle() + '\n' +
                "Характер: " + nature.getTitle() + '\n' +
                "Фобия: " + phobia.getTitle() + '\n' +
                "Дополнительно: " + extra.getTitle() + '\n' +
                "Инвентарь: " + inventory + '\n';
    }
}

class GenerationTest {
    public static void main(String[] args) {
        System.out.println(new Person("Andrew"));
        System.out.println(new Person("Fernando"));
        System.out.println(new Person("Loren"));
        System.out.println(new Person("Thomas"));
        System.out.println(new Person("Ann"));
    }
}
package ru.kpfu.itis.client;

import ru.kpfu.itis.enums.gameParameters.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Seeker {

    private String name;
    private Gender gender;
    private Integer age;
    private Past past;
    private Job job;
    private Nature nature;
    private Gossip gossip;
    private List<Item> inventory;
    private Dream dream;


    public Seeker(String username) {
        Random random = new Random();

        this.name = username;
        this.gender = Gender.random();
        this.age = random.nextInt(80) + 18;
        this.past = Past.random();
        this.job = Job.random();
        this.nature = Nature.random();
        this.gossip = Gossip.random();

        this.inventory = new ArrayList<>();
        this.inventory.add(Item.random());
        if (random.nextInt(100) < 30) {
            this.inventory.add(Item.random());
        }
        if (random.nextInt(100) < 5) {
            this.inventory.add(Item.random());
        }

        this.dream = Dream.random();
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
                "Работа: " + job.getTitle() + '\n' +
                "Характер: " + nature.getTitle() + '\n' +
                "Прошлое: " + past.getTitle() + '\n' +
                "Слухи: " + gossip.getTitle() + '\n' +
                "Инвентарь: " + inventory + '\n' +
                "Мечта: " + dream.getTitle() + '\n';
    }
}
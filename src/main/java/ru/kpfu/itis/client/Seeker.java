package ru.kpfu.itis.client;

import ru.kpfu.itis.enums.gameParameters.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Seeker {

    private UUID uuid;

    private String name;
    private Gender gender;
    private Integer age;
    private Job job;
    private Nature nature;
    private Past past;
    private Gossip gossip;
    private List<Item> inventory;
    private Dream dream;

    private Integer lootingRemaining = 1;
    private Boolean revealedOwnInfo = false;
    private Boolean readyForVote = false;

    private boolean[] revealed = new boolean[6];

    public Seeker() {
    }

    public Seeker(String username) {
        Random random = new Random();

        uuid = UUID.randomUUID();

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

    public Integer getLootingRemaining() {
        return lootingRemaining;
    }

    public void setLootingRemaining(Integer lootingRemaining) {
        this.lootingRemaining = lootingRemaining;
    }

    public Boolean getRevealedOwnInfo() {
        return revealedOwnInfo;
    }

    public void setRevealedOwnInfo(Boolean revealedOwnInfo) {
        this.revealedOwnInfo = revealedOwnInfo;
    }

    public boolean[] getRevealed() {
        return revealed;
    }

    public void setRevealed(boolean[] revealed) {
        this.revealed = revealed;
    }

    public Boolean getReadyForVote() {
        return readyForVote;
    }

    public void setReadyForVote(Boolean readyForVote) {
        this.readyForVote = readyForVote;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public Job getJob() {
        return job;
    }

    public Nature getNature() {
        return nature;
    }

    public Past getPast() {
        return past;
    }

    public Gossip getGossip() {
        return gossip;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public Dream getDream() {
        return dream;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
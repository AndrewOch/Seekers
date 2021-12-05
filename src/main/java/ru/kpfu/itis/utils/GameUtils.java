package ru.kpfu.itis.utils;

import javafx.scene.image.ImageView;
import ru.kpfu.itis.client.Seeker;
import ru.kpfu.itis.enums.gameParameters.Item;

import java.util.Random;

public class GameUtils {

    public void loot(Seeker seeker) {
        Random random = new Random();

        seeker.getInventory().add(Item.random());
        if (random.nextInt(100) < 30) {
            seeker.getInventory().add(Item.random());
        }
        if (random.nextInt(100) < 20) {
            seeker.getInventory().add(Item.random());
        }
        seeker.setLootingRemaining(seeker.getLootingRemaining() - 1);
    }

    public void spy(Seeker seeker, Integer parameterId) {
        seeker.getRevealed()[parameterId] = true;
    }

    public void readyForVote() {
        //TODO
    }

    public void spendItems(Seeker seeker) {
        seeker.getInventory().remove(0);
        seeker.getInventory().remove(0);
    }


}
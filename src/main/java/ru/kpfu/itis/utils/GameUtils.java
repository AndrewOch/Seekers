package ru.kpfu.itis.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.image.ImageView;
import ru.kpfu.itis.client.Seeker;
import ru.kpfu.itis.enums.gameParameters.Item;
import ru.kpfu.itis.protocols.Message;
import ru.kpfu.itis.protocols.MessageType;
import ru.kpfu.itis.socket.ClientSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUtils {

    private ClientSocket clientSocket;

    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void loot(Seeker seeker) {
        Random random = new Random();

        List<String> found = new ArrayList<>();

        Item item = Item.random();
        found.add(item.getTitle());
        seeker.getInventory().add(item);
        if (random.nextInt(100) < 30) {
            item = Item.random();
            found.add(item.getTitle());
            seeker.getInventory().add(item);
        }
        if (random.nextInt(100) < 20) {
            item = Item.random();
            found.add(item.getTitle());
            seeker.getInventory().add(item);
        }
        seeker.setLootingRemaining(seeker.getLootingRemaining() - 1);

        Message message = new Message();
        message.setType(MessageType.LOOT);
        message.addHeader("found", found.toString());
        try {
            message.setBody(new ObjectMapper().writeValueAsString(seeker));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        clientSocket.sendMessage(message);
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
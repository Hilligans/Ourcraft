package dev.hilligans.ourcraft.addons.bot;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.data.other.Inventory;
import dev.hilligans.ourcraft.util.EntityPosition;

public class BotInstance {

    public String name;
    public GameInstance gameInstance;
    public Runnable scriptAction;
    public BotActions botActions;
    public Client client;;

    public BotInstance(String name, GameInstance gameInstance) {
        this.name = name;
        this.gameInstance = gameInstance;
    }

    public Inventory botInventory() {
        return client.playerData.inventory;
    }

    public EntityPosition getPosition() {

        return null;
       // return client.
    }
}

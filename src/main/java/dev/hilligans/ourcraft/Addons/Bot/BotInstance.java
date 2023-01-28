package dev.hilligans.ourcraft.Addons.Bot;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Data.Other.Inventory;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.Util.EntityPosition;

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

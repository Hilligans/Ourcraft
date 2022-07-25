package dev.Hilligans.ourcraft.Addons.Bot;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Data.Other.Inventory;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Util.EntityPosition;

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

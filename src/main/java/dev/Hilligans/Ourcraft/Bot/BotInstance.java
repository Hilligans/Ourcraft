package dev.Hilligans.Ourcraft.Bot;

import dev.Hilligans.Ourcraft.Client.Client;
import dev.Hilligans.Ourcraft.GameInstance;

public class BotInstance {

    public String name;
    public GameInstance gameInstance;
    public Runnable scriptAction;
    public BotActions botActions;
    public Client client;

    public BotInstance(String name, GameInstance gameInstance) {
        this.name = name;
        this.gameInstance = gameInstance;
    }







}

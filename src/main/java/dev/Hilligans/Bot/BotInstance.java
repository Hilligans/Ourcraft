package dev.Hilligans.Bot;

import dev.Hilligans.Client.Client;
import dev.Hilligans.GameInstance;

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

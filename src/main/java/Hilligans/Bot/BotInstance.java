package Hilligans.Bot;

import Hilligans.Client.Client;
import Hilligans.GameInstance;

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

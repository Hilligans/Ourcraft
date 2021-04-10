package Hilligans.ModHandler.Events.Server;

import Hilligans.ModHandler.Event;
import Hilligans.Server.MultiPlayerServer;

public class ServerTickEvent extends Event {

    public MultiPlayerServer server;

    public ServerTickEvent(MultiPlayerServer server) {
        this.server = server;
    }


}

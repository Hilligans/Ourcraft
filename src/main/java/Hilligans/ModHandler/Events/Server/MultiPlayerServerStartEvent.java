package Hilligans.ModHandler.Events.Server;

import Hilligans.ModHandler.Event;
import Hilligans.Server.MultiPlayerServer;
import Hilligans.World.World;

public class MultiPlayerServerStartEvent extends Event {

    public MultiPlayerServer server;
    public String port;

    public MultiPlayerServerStartEvent(MultiPlayerServer server, String port) {
        this.server = server;
        this.port = port;
    }

}

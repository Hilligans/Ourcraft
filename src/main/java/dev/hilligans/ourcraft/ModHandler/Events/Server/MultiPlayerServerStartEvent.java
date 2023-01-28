package dev.hilligans.ourcraft.ModHandler.Events.Server;

import dev.hilligans.ourcraft.ModHandler.Event;
import dev.hilligans.ourcraft.Server.MultiPlayerServer;

public class MultiPlayerServerStartEvent extends Event {

    public MultiPlayerServer server;
    public String port;

    public MultiPlayerServerStartEvent(MultiPlayerServer server, String port) {
        this.server = server;
        this.port = port;
    }

}

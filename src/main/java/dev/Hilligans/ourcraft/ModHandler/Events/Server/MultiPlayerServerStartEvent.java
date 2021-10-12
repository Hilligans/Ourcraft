package dev.Hilligans.ourcraft.ModHandler.Events.Server;

import dev.Hilligans.ourcraft.ModHandler.Event;
import dev.Hilligans.ourcraft.Server.MultiPlayerServer;

public class MultiPlayerServerStartEvent extends Event {

    public MultiPlayerServer server;
    public String port;

    public MultiPlayerServerStartEvent(MultiPlayerServer server, String port) {
        this.server = server;
        this.port = port;
    }

}

package dev.Hilligans.ModHandler.Events.Server;

import dev.Hilligans.ModHandler.Event;
import dev.Hilligans.Server.MultiPlayerServer;

public class MultiPlayerServerStartEvent extends Event {

    public MultiPlayerServer server;
    public String port;

    public MultiPlayerServerStartEvent(MultiPlayerServer server, String port) {
        this.server = server;
        this.port = port;
    }

}

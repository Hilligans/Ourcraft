package dev.Hilligans.Ourcraft.ModHandler.Events.Server;

import dev.Hilligans.Ourcraft.ModHandler.Event;
import dev.Hilligans.Ourcraft.Server.MultiPlayerServer;

public class MultiPlayerServerStartEvent extends Event {

    public MultiPlayerServer server;
    public String port;

    public MultiPlayerServerStartEvent(MultiPlayerServer server, String port) {
        this.server = server;
        this.port = port;
    }

}

package dev.hilligans.ourcraft.mod.handler.events.server;

import dev.hilligans.ourcraft.mod.handler.Event;
import dev.hilligans.ourcraft.server.MultiPlayerServer;

public class MultiPlayerServerStartEvent extends Event {

    public MultiPlayerServer server;
    public String port;

    public MultiPlayerServerStartEvent(MultiPlayerServer server, String port) {
        this.server = server;
        this.port = port;
    }

}

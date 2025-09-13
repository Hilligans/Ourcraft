package dev.hilligans.engine.mod.handler.events.server;

import dev.hilligans.engine.mod.handler.Event;
import dev.hilligans.ourcraft.server.MultiPlayerServer;

public class MultiPlayerServerStartEvent extends Event {

    public MultiPlayerServer server;
    public String port;

    public MultiPlayerServerStartEvent(MultiPlayerServer server, String port) {
        this.server = server;
        this.port = port;
    }

}

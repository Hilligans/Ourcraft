package dev.hilligans.ourcraft.mod.handler.events.server;

import dev.hilligans.ourcraft.mod.handler.Event;
import dev.hilligans.ourcraft.server.IServer;

public class ServerTickEvent extends Event {

    public IServer server;

    public ServerTickEvent(IServer server) {
        this.server = server;
    }


}

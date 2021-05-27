package Hilligans.ModHandler.Events.Server;

import Hilligans.ModHandler.Event;
import Hilligans.Server.IServer;
import Hilligans.Server.MultiPlayerServer;

public class ServerTickEvent extends Event {

    public IServer server;

    public ServerTickEvent(IServer server) {
        this.server = server;
    }


}

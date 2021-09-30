package Hilligans.ModHandler.Events.Client;

import Hilligans.Client.Client;
import Hilligans.ModHandler.Event;

public class RenderPostEvent extends Event {
    public Client client;

    public RenderPostEvent(Client client) {
        this.client = client;
    }

}

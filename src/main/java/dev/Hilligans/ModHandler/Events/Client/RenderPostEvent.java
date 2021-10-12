package dev.Hilligans.ModHandler.Events.Client;

import dev.Hilligans.Client.Client;
import dev.Hilligans.ModHandler.Event;

public class RenderPostEvent extends Event {
    public Client client;

    public RenderPostEvent(Client client) {
        this.client = client;
    }

}

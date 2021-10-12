package dev.Hilligans.ourcraft.ModHandler.Events.Client;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.ModHandler.Event;

public class RenderPostEvent extends Event {
    public Client client;

    public RenderPostEvent(Client client) {
        this.client = client;
    }

}

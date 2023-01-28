package dev.hilligans.ourcraft.ModHandler.Events.Client;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.ModHandler.Event;

public class RenderPostEvent extends Event {
    public Client client;

    public RenderPostEvent(Client client) {
        this.client = client;
    }

}

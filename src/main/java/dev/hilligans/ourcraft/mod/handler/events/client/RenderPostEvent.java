package dev.hilligans.ourcraft.mod.handler.events.client;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.mod.handler.Event;

public class RenderPostEvent extends Event {
    public Client client;

    public RenderPostEvent(Client client) {
        this.client = client;
    }

}

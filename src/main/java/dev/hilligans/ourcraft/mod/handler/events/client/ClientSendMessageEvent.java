package dev.hilligans.ourcraft.mod.handler.events.client;

import dev.hilligans.ourcraft.mod.handler.Event;

public class ClientSendMessageEvent extends Event {

    public String message;

    public ClientSendMessageEvent(String message) {
        this.message = message;
    }


}

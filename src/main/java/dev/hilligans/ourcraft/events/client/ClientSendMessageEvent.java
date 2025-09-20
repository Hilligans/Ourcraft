package dev.hilligans.ourcraft.events.client;

import dev.hilligans.engine.mod.handler.Event;

public class ClientSendMessageEvent extends Event {

    public String message;

    public ClientSendMessageEvent(String message) {
        this.message = message;
    }


}

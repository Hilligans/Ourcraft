package dev.hilligans.ourcraft.ModHandler.Events.Client;

import dev.hilligans.ourcraft.ModHandler.Event;

public class ClientSendMessageEvent extends Event {

    public String message;

    public ClientSendMessageEvent(String message) {
        this.message = message;
    }


}
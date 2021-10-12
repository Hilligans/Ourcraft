package dev.Hilligans.ModHandler.Events.Client;

import dev.Hilligans.ModHandler.Event;

public class ClientSendMessageEvent extends Event {

    public String message;

    public ClientSendMessageEvent(String message) {
        this.message = message;
    }


}

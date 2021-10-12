package dev.Hilligans.ourcraft.ModHandler.Events.Client;

import dev.Hilligans.ourcraft.ModHandler.Event;

public class ClientSendMessageEvent extends Event {

    public String message;

    public ClientSendMessageEvent(String message) {
        this.message = message;
    }


}

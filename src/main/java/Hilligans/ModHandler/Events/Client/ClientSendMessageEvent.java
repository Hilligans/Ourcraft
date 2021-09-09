package Hilligans.ModHandler.Events.Client;

import Hilligans.ModHandler.Event;

public class ClientSendMessageEvent extends Event {

    public String message;

    public ClientSendMessageEvent(String message) {
        this.message = message;
    }


}

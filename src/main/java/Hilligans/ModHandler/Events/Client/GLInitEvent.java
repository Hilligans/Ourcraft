package Hilligans.ModHandler.Events.Client;

import Hilligans.ModHandler.Event;

public class GLInitEvent extends Event {

    public long window;

    public GLInitEvent(long window) {
        this.window = window;
    }

}

package Hilligans.EventHandler.Events;

import Hilligans.EventHandler.Event;

public class GLInitEvent extends Event {

    public long window;

    public GLInitEvent(long window) {
        this.window = window;
    }

}

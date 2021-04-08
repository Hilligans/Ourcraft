package Hilligans.ModHandler.Events;

import Hilligans.ModHandler.Event;

public class GLInitEvent extends Event {

    public long window;

    public GLInitEvent(long window) {
        this.window = window;
    }

}

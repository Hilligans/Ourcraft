package dev.hilligans.ourcraft.ModHandler.Events.Client;

import dev.hilligans.ourcraft.ModHandler.Event;

public class GLInitEvent extends Event {

    public long window;

    public GLInitEvent(long window) {
        this.window = window;
    }

}

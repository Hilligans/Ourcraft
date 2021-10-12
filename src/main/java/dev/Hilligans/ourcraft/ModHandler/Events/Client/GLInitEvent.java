package dev.Hilligans.ourcraft.ModHandler.Events.Client;

import dev.Hilligans.ourcraft.ModHandler.Event;

public class GLInitEvent extends Event {

    public long window;

    public GLInitEvent(long window) {
        this.window = window;
    }

}

package dev.Hilligans.Ourcraft.ModHandler.Events.Client;

import dev.Hilligans.Ourcraft.ModHandler.Event;

public class GLInitEvent extends Event {

    public long window;

    public GLInitEvent(long window) {
        this.window = window;
    }

}

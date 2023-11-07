package dev.hilligans.ourcraft.mod.handler.events.client;

import dev.hilligans.ourcraft.mod.handler.Event;

public class GLInitEvent extends Event {

    public long window;

    public GLInitEvent(long window) {
        this.window = window;
    }

}

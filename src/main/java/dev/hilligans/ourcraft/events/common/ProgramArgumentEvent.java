package dev.hilligans.ourcraft.events.common;

import dev.hilligans.engine.mod.handler.Event;

public class ProgramArgumentEvent extends Event {

    public String argument;

    public ProgramArgumentEvent(String argument) {
        this.argument = argument;
    }
}

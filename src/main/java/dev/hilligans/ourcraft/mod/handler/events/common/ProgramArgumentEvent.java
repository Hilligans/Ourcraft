package dev.hilligans.ourcraft.mod.handler.events.common;

import dev.hilligans.ourcraft.mod.handler.Event;

public class ProgramArgumentEvent extends Event {

    public String argument;

    public ProgramArgumentEvent(String argument) {
        this.argument = argument;
    }
}

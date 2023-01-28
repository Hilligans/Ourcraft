package dev.hilligans.ourcraft.ModHandler.Events.Common;

import dev.hilligans.ourcraft.ModHandler.Event;

public class ProgramArgumentEvent extends Event {

    public String argument;

    public ProgramArgumentEvent(String argument) {
        this.argument = argument;
    }
}

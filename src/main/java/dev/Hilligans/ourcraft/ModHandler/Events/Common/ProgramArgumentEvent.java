package dev.Hilligans.ourcraft.ModHandler.Events.Common;

import dev.Hilligans.ourcraft.ModHandler.Event;

public class ProgramArgumentEvent extends Event {

    public String argument;

    public ProgramArgumentEvent(String argument) {
        this.argument = argument;
    }
}

package dev.Hilligans.ourcraft.ModHandler.Events.Client;

import dev.Hilligans.ourcraft.Client.Rendering.Screen;
import dev.Hilligans.ourcraft.ModHandler.Event;

public class OpenScreenEvent extends Event {

    Screen newScreen;
    Screen oldScreen;

    public OpenScreenEvent(Screen newScreen, Screen oldScreen) {
        this.newScreen = newScreen;
        this.oldScreen = oldScreen;
    }


}

package dev.Hilligans.Ourcraft.ModHandler.Events.Client;

import dev.Hilligans.Ourcraft.Client.Rendering.Screen;
import dev.Hilligans.Ourcraft.ModHandler.Event;

public class OpenScreenEvent extends Event {

    Screen newScreen;
    Screen oldScreen;

    public OpenScreenEvent(Screen newScreen, Screen oldScreen) {
        this.newScreen = newScreen;
        this.oldScreen = oldScreen;
    }


}

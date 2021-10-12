package dev.Hilligans.ModHandler.Events.Client;

import dev.Hilligans.Client.Rendering.Screen;
import dev.Hilligans.ModHandler.Event;

public class OpenScreenEvent extends Event {

    Screen newScreen;
    Screen oldScreen;

    public OpenScreenEvent(Screen newScreen, Screen oldScreen) {
        this.newScreen = newScreen;
        this.oldScreen = oldScreen;
    }


}

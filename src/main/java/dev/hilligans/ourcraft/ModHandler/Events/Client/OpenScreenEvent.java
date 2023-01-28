package dev.hilligans.ourcraft.ModHandler.Events.Client;

import dev.hilligans.ourcraft.Client.Rendering.Screen;
import dev.hilligans.ourcraft.ModHandler.Event;

public class OpenScreenEvent extends Event {

    Screen newScreen;
    Screen oldScreen;

    public OpenScreenEvent(Screen newScreen, Screen oldScreen) {
        this.newScreen = newScreen;
        this.oldScreen = oldScreen;
    }


}

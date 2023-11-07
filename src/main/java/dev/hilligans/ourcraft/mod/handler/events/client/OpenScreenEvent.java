package dev.hilligans.ourcraft.mod.handler.events.client;

import dev.hilligans.ourcraft.client.rendering.Screen;
import dev.hilligans.ourcraft.mod.handler.Event;

public class OpenScreenEvent extends Event {

    Screen newScreen;
    Screen oldScreen;

    public OpenScreenEvent(Screen newScreen, Screen oldScreen) {
        this.newScreen = newScreen;
        this.oldScreen = oldScreen;
    }


}

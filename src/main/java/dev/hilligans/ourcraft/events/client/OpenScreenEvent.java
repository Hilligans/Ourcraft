package dev.hilligans.ourcraft.events.client;

import dev.hilligans.engine.client.graphics.Screen;
import dev.hilligans.engine.mod.handler.Event;

public class OpenScreenEvent extends Event {

    Screen newScreen;
    Screen oldScreen;

    public OpenScreenEvent(Screen newScreen, Screen oldScreen) {
        this.newScreen = newScreen;
        this.oldScreen = oldScreen;
    }


}

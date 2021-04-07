package Hilligans.EventHandler.Events;

import Hilligans.Client.Rendering.Screen;
import Hilligans.EventHandler.Event;

public class OpenScreenEvent extends Event {

    Screen newScreen;
    Screen oldScreen;

    public OpenScreenEvent(Screen newScreen, Screen oldScreen) {
        this.newScreen = newScreen;
        this.oldScreen = oldScreen;
    }


}

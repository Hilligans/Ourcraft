package Hilligans.ModHandler.Events.Client;

import Hilligans.Client.Rendering.Screen;
import Hilligans.ModHandler.Event;

public class OpenScreenEvent extends Event {

    Screen newScreen;
    Screen oldScreen;

    public OpenScreenEvent(Screen newScreen, Screen oldScreen) {
        this.newScreen = newScreen;
        this.oldScreen = oldScreen;
    }


}

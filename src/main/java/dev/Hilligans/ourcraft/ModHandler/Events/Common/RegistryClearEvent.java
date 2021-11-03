package dev.Hilligans.ourcraft.ModHandler.Events.Common;

import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Event;

public class RegistryClearEvent extends Event {

    public GameInstance gameInstance;

    public RegistryClearEvent(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }


}

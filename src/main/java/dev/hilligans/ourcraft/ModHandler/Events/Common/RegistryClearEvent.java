package dev.hilligans.ourcraft.ModHandler.Events.Common;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.ModHandler.Event;

public class RegistryClearEvent extends Event {

    public GameInstance gameInstance;

    public RegistryClearEvent(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }


}

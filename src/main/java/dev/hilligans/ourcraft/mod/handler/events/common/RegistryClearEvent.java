package dev.hilligans.ourcraft.mod.handler.events.common;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.Event;

public class RegistryClearEvent extends Event {

    public GameInstance gameInstance;

    public RegistryClearEvent(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }


}

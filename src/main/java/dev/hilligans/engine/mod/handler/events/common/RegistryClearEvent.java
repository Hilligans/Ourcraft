package dev.hilligans.engine.mod.handler.events.common;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.handler.Event;

public class RegistryClearEvent extends Event {

    public GameInstance gameInstance;

    public RegistryClearEvent(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }


}

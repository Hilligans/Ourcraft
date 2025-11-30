package dev.hilligans.engine.gametype;

import dev.hilligans.engine.util.registry.IRegistryElement;

public interface IGameType extends IRegistryElement {

    @Override
    default String getResourceType() {
        return "game_type";
    }
}

package dev.hilligans.engine.application;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.util.registry.IRegistryElement;

public interface IApplication extends IRegistryElement {

    void postCoreStartApplication(GameInstance gameInstance);

    void startApplication(GameInstance gameInstance);

    default String getResourceType() {
        return "application";
    }
}

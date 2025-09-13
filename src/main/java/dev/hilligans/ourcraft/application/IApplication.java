package dev.hilligans.ourcraft.application;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public interface IApplication extends IRegistryElement {

    void postCoreStartApplication(GameInstance gameInstance);

    void startApplication(GameInstance gameInstance);

    default String getResourceType() {
        return "application";
    }
}

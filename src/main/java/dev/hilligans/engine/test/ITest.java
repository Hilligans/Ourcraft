package dev.hilligans.engine.test;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public interface ITest extends IRegistryElement {

    void runTest(GameInstance gameInstance);

    @Override
    default String getResourceType() {
        return "test";
    }
}

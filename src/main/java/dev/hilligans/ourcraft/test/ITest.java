package dev.hilligans.ourcraft.test;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public interface ITest extends IRegistryElement {

    void runTest(GameInstance gameInstance);

    @Override
    default String getResourceType() {
        return "test";
    }
}

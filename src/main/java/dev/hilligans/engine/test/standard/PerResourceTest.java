package dev.hilligans.engine.test.standard;

import dev.hilligans.engine.Engine;
import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.test.ITest;
import dev.hilligans.engine.util.registry.ITestableRegistryElement;

public class PerResourceTest implements ITest {

    public static final PerResourceTest instance = new PerResourceTest();

    @Override
    public void runTest(GameInstance gameInstance) {
        gameInstance.REGISTRIES.forEach((registry -> registry.forEach(element -> {
            if (element instanceof ITestableRegistryElement testableRegistryElement) {
                testableRegistryElement.runTest(gameInstance);
            }
        })));
    }

    @Override
    public String getResourceName() {
        return "per_resource_test";
    }

    @Override
    public String getResourceOwner() {
        return Engine.ENGINE_NAME;
    }
}

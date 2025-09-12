package dev.hilligans.ourcraft.test.standard;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.ourcraft.mod.handler.pipeline.standard.StandardPipeline;
import dev.hilligans.ourcraft.test.ITest;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.util.registry.Registry;

public class DuplicateRegistryTest implements ITest {

    @Override
    public String getResourceName() {
        return "duplicate_registry_test";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }

    @Override
    public void runTest(GameInstance gameInstance) {
        GameInstance tempInstance = new GameInstance();
        tempInstance.side = gameInstance.side;
        InstanceLoaderPipeline<?> pipeline = StandardPipeline.get(tempInstance);
        pipeline.build();

        for(Registry<?> registry : gameInstance.REGISTRIES.ELEMENTS) {
            System.out.println("Comparing registry " + registry.getIdentifierName());
            Registry<?> secondaryRegistry = tempInstance.getRegistry(registry.classType);

            if(secondaryRegistry != null) {
                for(IRegistryElement element : registry.ELEMENTS) {
                    if(secondaryRegistry.contains(element)) {
                        throw new RuntimeException("Registry element " + element.getUniqueName() + " found registered across two different game instances.");
                    }
                }
            }
        }
    }
}

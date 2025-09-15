package dev.hilligans.engine.test.standard;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.engine.mod.handler.pipeline.standard.StandardPipeline;
import dev.hilligans.engine.test.ITest;
import dev.hilligans.engine.util.registry.IRegistryElement;
import dev.hilligans.engine.util.registry.Registry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
                        if(!testFinals(element.getClass())) {
                            throw new RuntimeException("Registry element " + element.getUniqueName() + " found registered across two different game instances.");
                        }
                    }
                }
            }
        }
    }

    /* Checks if all the fields in this object are final */
    public static boolean testFinals(Class<? extends IRegistryElement> clazz) {
        for(Field field : clazz.getDeclaredFields()) {
            if((field.getModifiers() & Modifier.FINAL) == 0) {
                return false;
            }
        }
        return true;
    }
}

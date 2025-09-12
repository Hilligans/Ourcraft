package dev.hilligans.ourcraft.mod.handler.pipeline.other;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.ourcraft.mod.handler.pipeline.standard.StandardPipeline;

public class TestPipeline {

    public static InstanceLoaderPipeline<?> get(GameInstance gameInstance) {
        InstanceLoaderPipeline<?> pipeline = StandardPipeline.get(gameInstance);

        pipeline.addPostHook((instance) -> {
            instance.TESTS.forEach(test -> {
                System.out.println("Running test " + test.getIdentifierName());
                test.runTest(instance);
            });
        });
        return pipeline;
    }
}

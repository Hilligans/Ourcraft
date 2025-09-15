package dev.hilligans.engine.mod.handler.pipeline.other;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.engine.mod.handler.pipeline.standard.StandardPipeline;

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

    public static void run(GameInstance gameInstance) {
        InstanceLoaderPipeline<?> pipeline = get(gameInstance);
        pipeline.build();
        System.exit(0);
    }
}

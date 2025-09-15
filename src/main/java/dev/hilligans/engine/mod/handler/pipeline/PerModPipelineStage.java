package dev.hilligans.engine.mod.handler.pipeline;

import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.util.sections.ISection;

import java.util.concurrent.atomic.AtomicInteger;

public interface PerModPipelineStage<T extends InstanceLoaderPipeline<T>> extends PipelineStage<T> {

    void execute(ModContainer modClass);

    @Override
    default void execute(T pipeline, ISection section) {
        int length = pipeline.getModList().getCount();
        int last = 0;
        AtomicInteger counter = new AtomicInteger();
        pipeline.getModList().foreach(mod -> Thread.startVirtualThread(() -> {
            execute(mod);
            counter.getAndIncrement();
        }));
        while (counter.get() != length) {
            try(var $1 = section.startSection(last + " of " + length)) {
            //try(var $1 = section.startSection(STR."\{last} of \{length}")) {
                try {
                    Thread.sleep(5);
                } catch (Exception ignored) {}
            }
        }
    }
}

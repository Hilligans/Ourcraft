package dev.hilligans.engine.mod.pipeline;

import dev.hilligans.engine.util.sections.ISection;

public interface AsyncPipelineStage<T>  extends PipelineStage<T> {

    default void execute(T pipeline, ISection section) {
        Thread.startVirtualThread(() -> executeAsync(pipeline, section));
    }

    void executeAsync(T pipeline, ISection section);
}

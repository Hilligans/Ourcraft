package dev.hilligans.engine.mod.pipeline;


import dev.hilligans.engine.util.sections.ISection;

public interface PipelineStage<T> {

     void execute(T pipeline, ISection section);

}

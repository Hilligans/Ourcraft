package dev.hilligans.engine.mod.handler.pipeline;


import dev.hilligans.engine.util.sections.ISection;

public interface PipelineStage<T> {

     void execute(T pipeline, ISection section);

}

package dev.hilligans.ourcraft.mod.handler.pipeline;


import dev.hilligans.ourcraft.util.sections.ISection;

public interface PipelineStage<T> {

     void execute(T pipeline, ISection section);

}

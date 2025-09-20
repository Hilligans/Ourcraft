package dev.hilligans.engine.util;

import java.util.ArrayList;
import java.util.Arrays;

public class Pipeline<I, O> {

    public ArrayList<PipelineStage<?,?>> pipelineStages = new ArrayList<>();

    public Pipeline<I, O> addPipelineStage(PipelineStage<I, ?> in, PipelineStage<?, O> out, PipelineStage<?,?>... pipelineStage) {
        pipelineStages.add(in);
        pipelineStages.addAll(Arrays.asList(pipelineStage));
        pipelineStages.add(out);

        PipelineStage<?,?> last = null;
        for(PipelineStage<?,?> stage : pipelineStages) {
            if(last != null) {
                last.out = stage.in;
            }
            last = stage;
        }
        last.out = this.out;
        this.in = in.in;
        return this;
    }

    public void start() {
        for(PipelineStage<?,?> stage : pipelineStages) {
            stage.start();
        }
    }

    public Pipeline<I, O> submit(I... data) {
        for(I val : data) {
            in.add(val);
        }
        return this;
    }

    public void shutdown() {
        for(PipelineStage<?,?> pipelineStage : pipelineStages) {
            pipelineStage.shutDownBlock();
        }
    }

    public DaisyChain<I> in;
    public DaisyChain<?> out = new DaisyChain<>();

}

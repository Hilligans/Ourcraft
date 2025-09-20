package dev.hilligans.engine.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class PipelineStage<I,O> {

    public static ExecutorService service = Executors.newFixedThreadPool(4, new NamedThreadFactory("pipeline_processor"));

    public void start() {
        start(service, 2);
    }

    public void start(ExecutorService executorService, int count) {
        this.count = count;
        this.executorService = executorService;
        for(int x = 0; x < count; x++) {
            service.submit(() -> {
                try {
                    while (!shutDown || !in.isEmpty()) {
                        I val = in.get();
                        if (val != null) {
                            PipelineStage.this.run(val, (DaisyChain<O>) out);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("done");
                complete.getAndIncrement();
            });
        }
    }

    public void shutDown() {
        shutDown = true;
    }

    public void shutDownBlock() {
        shutDown = true;
        while(complete.get() != count) {}
    }

    public boolean shutDown = false;
    public int count;
    public AtomicInteger complete = new AtomicInteger();
    public ExecutorService executorService;
    public DaisyChain<I> in = new DaisyChain<I>();
    public DaisyChain<?> out;

    public abstract void run(I in, DaisyChain<O> out);

}

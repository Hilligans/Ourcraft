package dev.hilligans.ourcraft.Client.Rendering;

import dev.hilligans.ourcraft.Util.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientUtil {


    public static ExecutorService chunkBuilder;
    public static ExecutorService randomExecutor;

    static {
        chunkBuilder = Executors.newSingleThreadExecutor(new NamedThreadFactory("chunk_builder"));
        randomExecutor = Executors.newFixedThreadPool(3, new NamedThreadFactory("random_executor"));
    }





}

package dev.Hilligans.Client.Rendering;

import dev.Hilligans.Util.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientUtil {


    public static ExecutorService chunkBuilder;
    public static ExecutorService randomExecutor;

    public static void register() {

    }

    static {
        chunkBuilder = Executors.newSingleThreadExecutor(new NamedThreadFactory("chunk_builder"));
        randomExecutor = Executors.newFixedThreadPool(3, new NamedThreadFactory("random_executor"));
    }





}

package Hilligans.Client.Rendering;

import Hilligans.Util.NamedThreadFactory;
import Hilligans.Util.Settings;

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

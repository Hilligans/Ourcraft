package dev.hilligans.ourcraft.Server.TickEngine.TickEngineParts;

import dev.hilligans.ourcraft.Server.IServer;
import dev.hilligans.ourcraft.Server.TickEngine.IGameProcessor;
import dev.hilligans.ourcraft.Server.TickEngine.IWorldProcessor;
import dev.hilligans.ourcraft.Server.TickEngine.TickEngineSettings;
import dev.hilligans.ourcraft.World.World;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TE2GameProcessor implements IGameProcessor {

    public TickEngineSettings tickEngineSettings;
    public ExecutorService worldProcessorThreadPool = Executors.newCachedThreadPool();
    public IWorldProcessor worldProcessor;

    public TE2GameProcessor(TickEngineSettings tickEngineSettings) {
        this.tickEngineSettings = tickEngineSettings;
        this.worldProcessor = new TickEngine1WorldProcessor(tickEngineSettings);
    }

    @Override
    public int tickServer(IServer server) {
        for(World world : server.getWorlds()) {
            worldProcessorThreadPool.submit(() -> worldProcessor.processWorld(world));
        }
        try {
            if(!worldProcessorThreadPool.awaitTermination(tickEngineSettings.tickTimeout, TimeUnit.MILLISECONDS)) {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}

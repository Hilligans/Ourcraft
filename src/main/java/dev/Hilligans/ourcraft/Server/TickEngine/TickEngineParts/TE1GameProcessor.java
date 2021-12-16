package dev.Hilligans.ourcraft.Server.TickEngine.TickEngineParts;

import dev.Hilligans.ourcraft.Server.IServer;
import dev.Hilligans.ourcraft.Server.TickEngine.IGameProcessor;
import dev.Hilligans.ourcraft.Server.TickEngine.TickEngineSettings;
import dev.Hilligans.ourcraft.World.World;

public class TE1GameProcessor implements IGameProcessor {

    public TickEngineSettings tickEngineSettings;
    public TickEngine1WorldProcessor worldProcessor;

    public TE1GameProcessor(TickEngineSettings tickEngineSettings) {
        this.tickEngineSettings = tickEngineSettings;
        this.worldProcessor = new TickEngine1WorldProcessor(tickEngineSettings);
    }

    @Override
    public int tickServer(IServer server) {
        for(World world : server.getWorlds()) {
            worldProcessor.processWorld(world);
        }
        return 0;
    }
}

package dev.hilligans.ourcraft.Command.CommandExecutors;

import dev.hilligans.ourcraft.World.World;
import dev.hilligans.ourcraft.Server.IServer;

public class ConsoleExecutor implements CommandExecutor {

    IServer multiPlayerServer;

    public ConsoleExecutor(IServer multiPlayerServer) {
        this.multiPlayerServer = multiPlayerServer;
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public double getZ() {
        return 0;
    }

    @Override
    public IServer getServer() {
        return multiPlayerServer;
    }

    @Override
    public World getWorld() {
        return multiPlayerServer.getDefaultWorld();
    }
}

package dev.Hilligans.Command.CommandExecutors;

import dev.Hilligans.Server.IServer;
import dev.Hilligans.World.World;

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

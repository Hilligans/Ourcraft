package dev.hilligans.ourcraft.command.executors;

import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import dev.hilligans.ourcraft.server.IServer;

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
    public IWorld getWorld() {
        return null;
       // return multiPlayerServer.getDefaultWorld();
    }
}

package dev.hilligans.ourcraft.command.executors;

import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import dev.hilligans.ourcraft.server.IServer;

public interface CommandExecutor {

     double getX();

     double getY();

     double getZ();

     IServer getServer();

     IWorld getWorld();
}

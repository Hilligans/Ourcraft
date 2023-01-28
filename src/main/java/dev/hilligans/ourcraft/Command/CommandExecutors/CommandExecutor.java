package dev.hilligans.ourcraft.Command.CommandExecutors;

import dev.hilligans.ourcraft.World.World;
import dev.hilligans.ourcraft.Server.IServer;

public interface CommandExecutor {

     double getX();

     double getY();

     double getZ();

     IServer getServer();

     World getWorld();
}

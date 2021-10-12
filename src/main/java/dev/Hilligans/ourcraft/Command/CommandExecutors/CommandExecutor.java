package dev.Hilligans.ourcraft.Command.CommandExecutors;

import dev.Hilligans.ourcraft.World.World;
import dev.Hilligans.ourcraft.Server.IServer;

public interface CommandExecutor {

     double getX();

     double getY();

     double getZ();

     IServer getServer();

     World getWorld();
}

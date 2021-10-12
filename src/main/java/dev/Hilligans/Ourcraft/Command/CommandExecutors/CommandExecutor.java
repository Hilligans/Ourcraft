package dev.Hilligans.Ourcraft.Command.CommandExecutors;

import dev.Hilligans.Ourcraft.World.World;
import dev.Hilligans.Ourcraft.Server.IServer;

public interface CommandExecutor {

     double getX();

     double getY();

     double getZ();

     IServer getServer();

     World getWorld();
}

package dev.Hilligans.Command.CommandExecutors;

import dev.Hilligans.Server.IServer;
import dev.Hilligans.World.World;

public interface CommandExecutor {

     double getX();

     double getY();

     double getZ();

     IServer getServer();

     World getWorld();
}

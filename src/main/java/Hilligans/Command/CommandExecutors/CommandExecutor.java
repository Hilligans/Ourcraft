package Hilligans.Command.CommandExecutors;

import Hilligans.Server.IServer;
import Hilligans.Server.MultiPlayerServer;
import Hilligans.World.World;

public interface CommandExecutor {

     double getX();

     double getY();

     double getZ();

     IServer getServer();

     World getWorld();
}

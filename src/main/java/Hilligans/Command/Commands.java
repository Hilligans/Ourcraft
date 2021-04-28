package Hilligans.Command;

import Hilligans.Command.CommandTypes.GameModeCommand;
import Hilligans.Command.CommandTypes.TeleportCommand;

import java.util.HashMap;

public class Commands {

    public static HashMap<String, CommandHandler> commands = new HashMap<>();

    public static final CommandHandler GAMEMODE = new GameModeCommand("gamemode");
    public static final CommandHandler TELEPORT = new TeleportCommand("teleport").addAlias("tp");

}

package dev.Hilligans.ourcraft.Command;

import dev.Hilligans.ourcraft.Addons.WorldEdit.LoadCommand;
import dev.Hilligans.ourcraft.Addons.WorldEdit.PosCommand;
import dev.Hilligans.ourcraft.Addons.WorldEdit.SetCommand;
import dev.Hilligans.ourcraft.Addons.WorldEdit.WorldEditData;
import dev.Hilligans.ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.Hilligans.ourcraft.Command.CommandTypes.GameModeCommand;
import dev.Hilligans.ourcraft.Command.CommandTypes.StopCommand;
import dev.Hilligans.ourcraft.Command.CommandTypes.TeleportCommand;

import java.util.HashMap;

public class Commands {

    public static HashMap<String, CommandHandler> commands = new HashMap<>();

    public static final CommandHandler GAMEMODE = new GameModeCommand("gamemode");
    public static final CommandHandler TELEPORT = new TeleportCommand("teleport").addAlias("tp");
    public static final CommandHandler STOP = new StopCommand("stop");

    public static final WorldEditData worldEditData = new WorldEditData();

    public static final CommandHandler SET = new SetCommand(worldEditData);
    public static final CommandHandler POS1 = new PosCommand(1,worldEditData);
    public static final CommandHandler POS2 = new PosCommand(2,worldEditData);
    public static final CommandHandler LOAD = new LoadCommand(worldEditData);


    public static Object executeCommand(String command, CommandExecutor commandExecutor) {
        try {
            String[] strings = command.split(" ");
            String[] args = new String[strings.length - 1];
            System.arraycopy(strings, 1, args, 0, args.length);
            CommandHandler commandHandler = commands.get(strings[0]);
            if (commandHandler != null) {
                return commandHandler.handle(commandExecutor, args);
            }
        } catch (Exception ignored) {}
        return null;
    }

}

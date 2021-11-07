package dev.Hilligans.ourcraft.Command.CommandTypes;

import dev.Hilligans.ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.Hilligans.ourcraft.Command.CommandHandler;

public class StopCommand extends CommandHandler {

    public StopCommand(String command) {
        super(command);
    }

    @Override
    public Object handle(CommandExecutor executor, String[] args) {
        System.exit(1);
        return "";
    }
}

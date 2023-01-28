package dev.hilligans.ourcraft.Command.CommandTypes;

import dev.hilligans.ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.hilligans.ourcraft.Command.CommandHandler;

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

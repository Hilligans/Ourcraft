package dev.hilligans.ourcraft.command.types;

import dev.hilligans.ourcraft.command.executors.CommandExecutor;
import dev.hilligans.ourcraft.command.CommandHandler;

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

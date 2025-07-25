package dev.hilligans.ourcraft.command;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.command.executors.CommandExecutor;

import java.util.Arrays;

public class CommandTree {

    GameInstance gameInstance;

    public CommandTree(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void execute(CommandExecutor executor, String[] args) {
        String[] actualArgs = Arrays.copyOfRange(args, 1, args.length);

        for(ICommand command : gameInstance.COMMANDS.ELEMENTS) {
            for(String alias : command.getAliases()) {
                if(alias.equals(args[0])) {
                    try {
                        if (command.execute(executor, actualArgs)) {
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}

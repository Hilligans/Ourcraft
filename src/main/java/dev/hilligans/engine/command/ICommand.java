package dev.hilligans.engine.command;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.command.executors.CommandExecutor;
import dev.hilligans.engine.util.registry.IRegistryElement;


public interface ICommand extends IRegistryElement {

    String[] getPermissions();
    ICommand setPermissions(String... permissions);

    String[] getAliases();
    ICommand setAliases(String... aliases);

    boolean execute(CommandExecutor executor, String[] args);

    GameInstance getGameInstance();

    default String getResourceType() {
        return "command";
    }
}

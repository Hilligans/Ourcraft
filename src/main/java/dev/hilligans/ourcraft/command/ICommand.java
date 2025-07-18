package dev.hilligans.ourcraft.command;

import dev.hilligans.ourcraft.command.executors.CommandExecutor;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;


public interface ICommand extends IRegistryElement {

    String[] getPermissions();
    ICommand setPermissions(String... permissions);

    String[] getAliases();
    ICommand setAliases(String... aliases);

    boolean execute(CommandExecutor executor, String[] args);

    default String getResourceType() {
        return "command";
    }
}

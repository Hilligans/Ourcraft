package dev.hilligans.ourcraft.command;

import dev.hilligans.ourcraft.command.executors.CommandExecutor;
import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.mod.handler.ModID;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

import java.util.ArrayList;

public abstract class CommandHandler implements IRegistryElement {

    public ModContent modContent;
    public ArrayList<String> aliases;
    public String command;
    public ModID mod;

    public CommandHandler(String command) {
        this.command = command;
        Commands.commands.put("/" + command,this);
    }

    public CommandHandler addAlias(String alias) {
        Commands.commands.put("/" + alias,this);
        return this;
    }

    public abstract Object handle(CommandExecutor executor, String[] args);

    public static boolean isNumber(String arg) {
        try {
            Float.parseFloat(arg);
            return true;
        } catch (Exception ignored) {}
        return false;
    }

    public ArrayList<Entity> processSelector(String selector) {

        return null;
    }

    public Entity processSelectorSingle(String selector) {
        return null;
    }

    @Override
    public void assignModContent(ModContent modContent) {
        this.modContent = modContent;
    }

    @Override
    public String getResourceName() {
        return command;
    }

    @Override
    public String getIdentifierName() {
        return modContent.getModID() + ":" + command;
    }

    @Override
    public String getUniqueName() {
        return "command." + modContent.getModID() + "." + command;
    }
}

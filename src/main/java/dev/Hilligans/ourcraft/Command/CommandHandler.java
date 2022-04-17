package dev.Hilligans.ourcraft.Command;

import dev.Hilligans.ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.Hilligans.ourcraft.Data.Other.BlockProperties;
import dev.Hilligans.ourcraft.Entity.Entity;
import dev.Hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.ModHandler.Mod;
import dev.Hilligans.ourcraft.ModHandler.ModID;
import dev.Hilligans.ourcraft.Ourcraft;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;
import dev.Hilligans.ourcraft.Util.Side;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.function.Consumer;

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

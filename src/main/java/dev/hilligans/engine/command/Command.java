package dev.hilligans.engine.command;


import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.handler.content.ModContainer;

public abstract class Command implements ICommand {

    public String name;
    public ModContainer owner;
    public String[] permissions;
    public String[] aliases;

    public Command(String name) {
        this.name = name;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return owner.getModID();
    }

    @Override
    public String[] getPermissions() {
        return permissions;
    }

    @Override
    public ICommand setPermissions(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public ICommand setAliases(String... aliases) {
        this.aliases = aliases;
        return this;
    }

    @Override
    public void assignOwner(ModContainer owner) {
        this.owner = owner;
    }


    public GameInstance getGameInstance() {
        return owner.getGameInstance();
    }
}

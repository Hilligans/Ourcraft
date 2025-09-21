package dev.hilligans.engine.resource.registry.loaders;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.handler.Identifier;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.util.registry.IRegistryElement;

public abstract class RegistryLoader implements IRegistryElement {

    public String name;
    public ModContainer source;

    public RegistryLoader(String name) {
        this.name = name;
    }

    public abstract void run();

    public GameInstance getGameInstance() {
        return source.getGameInstance();
    }

    @Override
    public void assignOwner(ModContainer source) {
        this.source = source;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return source.getModID();
    }

    @Override
    public String getResourceType() {
        return "registry_loader";
    }
}

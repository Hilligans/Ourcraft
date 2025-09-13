package dev.hilligans.engine.resource.registry.loaders;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.handler.Identifier;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public abstract class RegistryLoader implements IRegistryElement {

    public Identifier name;
    public boolean rerunOnInstanceClear;
    public GameInstance gameInstance;
    public ModContainer source;

    public RegistryLoader(Identifier name) {
        this.name = name;
    }

    public RegistryLoader rerunOnInstanceClear() {
        rerunOnInstanceClear = true;
        return this;
    }

    public abstract void run();

    public void runInit() {
        if(!rerunOnInstanceClear) {
            run();
        }
    }

    @Override
    public void assignOwner(ModContainer source) {
        this.source = source;
    }

    @Override
    public String getResourceName() {
        return name.getName();
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

package dev.hilligans.ourcraft.resource.registry.loaders;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.Identifier;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public abstract class RegistryLoader implements IRegistryElement {

    public Identifier name;
    public boolean rerunOnInstanceClear;
    public GameInstance gameInstance;
    public ModContent source;

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
    public void assignModContent(ModContent modContent) {
        this.source = modContent;
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

package dev.hilligans.ourcraft.resource.registry.loaders;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.Identifier;

public abstract class RegistryLoader {

    public Identifier name;
    public boolean rerunOnInstanceClear;
    public GameInstance gameInstance;

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
}

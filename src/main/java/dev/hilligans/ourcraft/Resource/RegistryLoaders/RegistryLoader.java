package dev.hilligans.ourcraft.Resource.RegistryLoaders;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.ModHandler.Identifier;

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

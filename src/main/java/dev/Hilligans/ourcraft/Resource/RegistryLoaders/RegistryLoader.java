package dev.Hilligans.ourcraft.Resource.RegistryLoaders;

import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Identifier;

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

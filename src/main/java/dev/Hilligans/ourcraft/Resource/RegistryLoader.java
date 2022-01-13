package dev.Hilligans.ourcraft.Resource;

import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Identifier;

public abstract class RegistryLoader {

    public Identifier name;
    public GameInstance gameInstance;

    public RegistryLoader(Identifier name) {
        this.name = name;
    }

    public abstract void run();
}

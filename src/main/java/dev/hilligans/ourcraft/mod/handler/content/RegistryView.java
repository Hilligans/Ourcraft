package dev.hilligans.ourcraft.mod.handler.content;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.util.registry.Registry;

import java.util.function.Supplier;

public class RegistryView {

    public GameInstance gameInstance;
    public String owner;

    public RegistryView(GameInstance gameInstance, String owner) {
        this.gameInstance = gameInstance;
        this.owner = owner;
    }

    public GameInstance getGameInstance() {
        return gameInstance;
    }

    public void registerRegistry(Supplier<Registry<?>> registry) {
        gameInstance.REGISTRIES.put(registry.get().assignOwner(owner));
    }

    public void registerRegistry(Supplier<Registry<?>>... registries) {
        for(Supplier<Registry<?>> registrySupplier : registries) {
            gameInstance.REGISTRIES.put(registrySupplier.get().assignOwner(owner));
        }
    }
}

package dev.hilligans.ourcraft.mod.handler.content;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.util.registry.Registry;

import java.util.function.Supplier;

public class RegistryView {

    public GameInstance gameInstance;

    public RegistryView(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public GameInstance getGameInstance() {
        return gameInstance;
    }

    public void registerRegistry(Supplier<Registry<?>> registry) {
        gameInstance.REGISTRIES.put(registry.get());
    }

    public void registerRegistry(Supplier<Registry<?>>... registries) {
        for(Supplier<Registry<?>> registrySupplier : registries) {
            gameInstance.REGISTRIES.put(registrySupplier.get());
        }
    }
}

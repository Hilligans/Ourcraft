package dev.hilligans.engine.resource.registry.loaders;

import dev.hilligans.engine.mod.handler.Identifier;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.ModContent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.BiConsumer;

public abstract class ModRegistryLoader<T> extends RegistryLoader {

    public HashMap<String, BiConsumer<ModContainer,T>> loaderMap = new HashMap<>();
    public BiConsumer<ModContainer,T> defaultConsumer;

    public ModRegistryLoader(String name) {
        super(name);
    }

    public void registerLoader(BiConsumer<ModContainer, T> consumer) {
        this.defaultConsumer = consumer;
    }

    public void replaceLoader(String modID, BiConsumer<ModContainer,T> function) {
        loaderMap.put(modID, function);
    }

    @Override
    public void run() {
        getGameInstance().MOD_LIST.mods.forEach((mod) -> {
            BiConsumer<ModContainer,T> consumer = loaderMap.getOrDefault(mod.getModID(), defaultConsumer);
            T resource = provideResource(mod);
            if(resource != null) {
                consumer.accept(mod, resource);
            }
        });
    }

    @Nullable
    public abstract T provideResource(ModContainer modContent);
}

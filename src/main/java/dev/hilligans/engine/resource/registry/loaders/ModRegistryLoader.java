package dev.hilligans.engine.resource.registry.loaders;

import dev.hilligans.engine.mod.handler.Identifier;
import dev.hilligans.engine.mod.handler.content.ModContent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.BiConsumer;

public abstract class ModRegistryLoader<T> extends RegistryLoader {

    public HashMap<String, BiConsumer<ModContent,T>> loaderMap = new HashMap<>();
    public BiConsumer<ModContent,T> defaultConsumer;

    public ModRegistryLoader(Identifier name) {
        super(name);
    }

    public void registerLoader(BiConsumer<ModContent, T> consumer) {
        this.defaultConsumer = consumer;
    }

    public void replaceLoader(String modID, BiConsumer<ModContent,T> function) {
        loaderMap.put(modID, function);
    }

    @Override
    public void run() {
        gameInstance.CONTENT_PACK.mods.forEach((string, modContent) -> {
            BiConsumer<ModContent,T> consumer = loaderMap.getOrDefault(string, defaultConsumer);
            T resource = provideResource(modContent);
            if(resource != null) {
                consumer.accept(modContent, resource);
            }
        });
    }

    @Nullable
    public abstract T provideResource(ModContent modContent);
}

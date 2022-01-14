package dev.Hilligans.ourcraft.Resource.RegistryLoaders;

import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.ModHandler.Identifier;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

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
            consumer.accept(modContent,provideResource(modContent));
        });
    }

    public abstract T provideResource(ModContent modContent);
}

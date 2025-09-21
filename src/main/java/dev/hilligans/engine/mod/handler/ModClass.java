package dev.hilligans.engine.mod.handler;

import dev.hilligans.engine.data.Tuple;
import dev.hilligans.engine.mod.handler.content.CoreExtensionView;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.RegistryView;
import dev.hilligans.engine.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.engine.util.registry.IRegistryElement;
import dev.hilligans.engine.util.registry.Registry;

public interface ModClass {

    String getModID();

    default void registerHooks(InstanceLoaderPipeline<?> pipeline) {
    }

    default void registerRegistries(RegistryView view) {
    }

    default void registerCoreExtensions(CoreExtensionView view) {
    }

    default void registerContent(ModContainer container) {
    }

    default void register(RegistryView view, Tuple<Class<? extends IRegistryElement>, String>... registries) {
        for(Tuple<Class<? extends IRegistryElement>, String> element : registries) {
            view.registerRegistry(() -> new Registry<>(view.getGameInstance(), element.getTypeA(), element.getTypeB()));
        }
    }
}

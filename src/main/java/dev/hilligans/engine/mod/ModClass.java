package dev.hilligans.engine.mod;

import dev.hilligans.engine.mod.content.CoreExtensionView;
import dev.hilligans.engine.mod.content.ModContainer;
import dev.hilligans.engine.mod.content.RegistryView;
import dev.hilligans.engine.mod.pipeline.InstanceLoaderPipeline;

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
}

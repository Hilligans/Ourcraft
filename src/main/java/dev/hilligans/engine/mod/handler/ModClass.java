package dev.hilligans.engine.mod.handler;

import dev.hilligans.engine.mod.handler.content.CoreExtensionView;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.RegistryView;
import dev.hilligans.engine.mod.handler.pipeline.InstanceLoaderPipeline;

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

package dev.hilligans.engine.mod.handler;

import dev.hilligans.engine.mod.handler.content.CoreExtensionView;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.RegistryView;
import dev.hilligans.engine.mod.handler.pipeline.InstanceLoaderPipeline;

public abstract class ModClass {

    public abstract String getModID();

    public void registerHooks(InstanceLoaderPipeline<?> pipeline) {
    }

    public void registerRegistries(RegistryView view) {
    }

    public void registerCoreExtensions(CoreExtensionView view) {
    }

    public void registerContent(ModContainer container) {
    }
}

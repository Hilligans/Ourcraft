package dev.hilligans.engine;

import dev.hilligans.engine.mod.handler.ModClass;
import dev.hilligans.engine.mod.handler.content.CoreExtensionView;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.RegistryView;

public class Engine extends ModClass {

    public static final String ENGINE_NAME = "engine";

    @Override
    public void registerRegistries(RegistryView view) {

    }

    @Override
    public void registerCoreExtensions(CoreExtensionView view) {

    }

    @Override
    public void registerContent(ModContainer container) {

    }

    @Override
    public String getModID() {
        return ENGINE_NAME;
    }
}

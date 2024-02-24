package dev.hilligans.ourcraft.mod.handler;

import dev.hilligans.ourcraft.mod.handler.content.CoreExtensionView;
import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.RegistryView;

public abstract class ModClass {

    public abstract String getModID();

    public void registerRegistries(RegistryView view) {

    }

    public void registerCoreExtensions(CoreExtensionView view) {

    }

    public void registerContent(ModContainer container) {

    }
}

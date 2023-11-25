package dev.hilligans.ourcraft.mod.handler.events.common;

import dev.hilligans.ourcraft.mod.handler.Event;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.util.registry.Registry;

public class RegisterEvent<T extends IRegistryElement> extends Event {

    public Registry<T> registry;
    public T registryObject;
    public String registryObjectName;

    public RegisterEvent(Registry<T> registry, String registryObjectName, T registryObject) {
        this.registry = registry;
        this.registryObjectName = registryObjectName;
        this.registryObject = registryObject;
    }

}

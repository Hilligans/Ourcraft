package dev.hilligans.ourcraft.events.common;

import dev.hilligans.engine.mod.handler.Event;
import dev.hilligans.engine.util.registry.IRegistryElement;
import dev.hilligans.engine.util.registry.Registry;

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

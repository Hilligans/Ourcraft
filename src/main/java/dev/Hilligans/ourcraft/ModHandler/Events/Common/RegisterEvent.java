package dev.Hilligans.ourcraft.ModHandler.Events.Common;

import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Event;
import dev.Hilligans.ourcraft.Util.Registry.Registry;

public class RegisterEvent<T> extends Event {

    public Registry<T> registry;
    public T registryObject;
    public String registryObjectName;

    public RegisterEvent(Registry<T> registry, String registryObjectName, T registryObject) {
        this.registry = registry;
        this.registryObjectName = registryObjectName;
        this.registryObject = registryObject;
    }

}

package dev.Hilligans.ourcraft.ModHandler.Content;

import dev.Hilligans.ourcraft.Util.Registry.Registry;

public class UnknownResourceException extends RuntimeException {

    public Registry<?> registry;
    public String resourceName;

    public UnknownResourceException(String message, Registry<?> registry, String resourceName) {
        super(message);
        this.registry = registry;
        this.resourceName = resourceName;
    }
}

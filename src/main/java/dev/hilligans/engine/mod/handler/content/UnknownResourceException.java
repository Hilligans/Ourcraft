package dev.hilligans.engine.mod.handler.content;

import dev.hilligans.ourcraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class UnknownResourceException extends RuntimeException {

    public Registry<?> registry;
    public String resourceName;
    public ModContent accessor;
    public ModContainer owner;

    public UnknownResourceException(String message, Registry<?> registry, String resourceName, @Nullable ModContent accessorMod) {
        super(message);
        this.registry = registry;
        this.resourceName = resourceName;
        this.accessor = accessorMod;
    }

    public UnknownResourceException(String message, Registry<?> registry, String resourceName, @Nullable ModContainer accessorMod) {
        super(message);
        this.registry = registry;
        this.resourceName = resourceName;
        this.owner = accessorMod;
    }
}

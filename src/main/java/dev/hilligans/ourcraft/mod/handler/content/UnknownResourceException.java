package dev.hilligans.ourcraft.mod.handler.content;

import dev.hilligans.ourcraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class UnknownResourceException extends RuntimeException {

    public Registry<?> registry;
    public String resourceName;
    public ModContent accessor;

    public UnknownResourceException(String message, Registry<?> registry, String resourceName, @Nullable ModContent accessorMod) {
        super(message);
        this.registry = registry;
        this.resourceName = resourceName;
        this.accessor = accessorMod;
    }
}

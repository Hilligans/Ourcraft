package dev.hilligans.engine.mod.handler.exception;

import dev.hilligans.engine.util.registry.IRegistryElement;

public class ResourceInitializationException extends RuntimeException {

    public IRegistryElement element;

    public ResourceInitializationException(IRegistryElement element, String message) {
        super(message, getAllocation(element));
        this.element = element;
    }

    public ResourceInitializationException(IRegistryElement element, Exception e) {
        super(e.getMessage(), getAllocation(element));
        this.element = element;
    }

    public static Throwable getAllocation(IRegistryElement element) {
        if(IRegistryElement.tracking) {
            return IRegistryElement.objectAllocationTrack.getOrDefault(element, null);
        }
        return null;
    }
}

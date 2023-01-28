package dev.hilligans.ourcraft.Util.Registry;

public class RegistryException extends RuntimeException {

    public Registry<?> registry;

    public RegistryException(String cause, Registry<?> registry) {
        super(cause);
        this.registry = registry;
    }
}

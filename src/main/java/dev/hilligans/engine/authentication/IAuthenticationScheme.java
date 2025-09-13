package dev.hilligans.engine.authentication;

import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public interface IAuthenticationScheme<T extends IAccount<?>> extends IRegistryElement {

    T authenticate(String username, IByteArray content);

    default String getResourceType() {
        return "authentication_scheme";
    }
}

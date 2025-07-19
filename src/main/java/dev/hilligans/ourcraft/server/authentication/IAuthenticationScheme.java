package dev.hilligans.ourcraft.server.authentication;

import dev.hilligans.ourcraft.util.IByteArray;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public interface IAuthenticationScheme<T extends IAccount<?>> extends IRegistryElement {

    T authenticate(String username, IByteArray content);

    default String getResourceType() {
        return "authentication_scheme";
    }
}

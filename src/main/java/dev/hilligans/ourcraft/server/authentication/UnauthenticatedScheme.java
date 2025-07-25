package dev.hilligans.ourcraft.server.authentication;

import dev.hilligans.ourcraft.util.IByteArray;

public class UnauthenticatedScheme implements IAuthenticationScheme<BasicAccount> {

    public long ID;

    @Override
    public BasicAccount authenticate(String username, IByteArray content) {
        return new BasicAccount(username, this, ID++);
    }

    @Override
    public String getResourceName() {
        return "unathenticated_scheme";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }
}

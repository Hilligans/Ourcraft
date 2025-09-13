package dev.hilligans.engine.authentication;

import dev.hilligans.engine.util.IByteArray;

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

    @Override
    public String toString() {
        return "UnauthenticatedScheme{" +
                "name=" + getIdentifierName() +
                '}';
    }
}

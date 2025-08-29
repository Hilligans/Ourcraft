package dev.hilligans.ourcraft.server.authentication;

public interface IAccount<T> {

    String getUsername();

    IAuthenticationScheme<?> getAuthenticationScheme();

    T getAccountID();

    default String getUniqueString() {
        return getAuthenticationScheme().getResourceName() + ":" + getUsername();
    }
}

package dev.hilligans.engine.authentication;

public class BasicAccount implements IAccount<Long> {

    public String username;
    public IAuthenticationScheme<BasicAccount> authenticationScheme;
    public long accountID;

    public BasicAccount(String username, IAuthenticationScheme<BasicAccount> authenticationScheme, long accountID) {
        this.username = username;
        this.authenticationScheme = authenticationScheme;
        this.accountID = accountID;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public IAuthenticationScheme<?> getAuthenticationScheme() {
        return authenticationScheme;
    }

    @Override
    public Long getAccountID() {
        return accountID;
    }
}

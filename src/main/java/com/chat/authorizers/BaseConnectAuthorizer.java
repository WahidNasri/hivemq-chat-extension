package com.chat.authorizers;

public abstract class BaseConnectAuthorizer {
    protected String username, password, clientId;

    public BaseConnectAuthorizer(String username, String password, String clientId) {
        this.username = username;
        this.password = password;
        this.clientId = clientId;
    }
    public abstract Result authorize();
}

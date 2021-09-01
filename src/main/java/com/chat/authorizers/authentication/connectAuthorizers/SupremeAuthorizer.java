package com.chat.authorizers.authentication.connectAuthorizers;

import com.chat.authorizers.BaseConnectAuthorizer;
import com.chat.authorizers.Result;

public class SupremeAuthorizer extends BaseConnectAuthorizer {
    public SupremeAuthorizer(String username, String password, String clientId) {
        super(username, password, clientId);
    }

    @Override
    public Result authorize() {
        return Result.AUTHORIZE;
    }
}

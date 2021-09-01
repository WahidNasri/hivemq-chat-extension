package com.chat.authorizers.pubsub.publishAuthorizers;

import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;

public class JustAllowAuthorizer extends BaseChatAuthorize {
    public JustAllowAuthorizer(String topic, String clientId, String userId) {
        super(topic, clientId, userId);
    }

    @Override
    public Result authorize() {
        return Result.AUTHORIZE;
    }
}

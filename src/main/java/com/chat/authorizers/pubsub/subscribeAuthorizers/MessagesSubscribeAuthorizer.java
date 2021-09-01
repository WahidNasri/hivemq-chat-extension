package com.chat.authorizers.pubsub.subscribeAuthorizers;

import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;

public class MessagesSubscribeAuthorizer extends BaseChatAuthorize {
    public MessagesSubscribeAuthorizer(String topic, String clientId, String userId) {
        super(topic, clientId, userId);
    }

    @Override
    public Result authorize() {
        return Result.AUTHORIZE;
    }
}

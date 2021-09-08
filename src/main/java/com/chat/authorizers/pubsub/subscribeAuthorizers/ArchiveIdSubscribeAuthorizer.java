package com.chat.authorizers.pubsub.subscribeAuthorizers;

import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;

public class ArchiveIdSubscribeAuthorizer extends BaseChatAuthorize {
    public ArchiveIdSubscribeAuthorizer(String topic, String clientId, String userId) {
        super(topic, clientId, userId);
    }

    @Override
    public Result authorize() {
        String foundClientId = topic.split("/")[1];
        if (foundClientId.equals(clientId)) {
            return Result.AUTHORIZE;
        } else {
            return Result.REJECT;
        }
    }
}

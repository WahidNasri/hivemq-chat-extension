package com.chat.authorizers.pubsub.publishAuthorizers;

import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;

public class PresencePublishAuthorizer extends BaseChatAuthorize {
    public PresencePublishAuthorizer(String topic, String clientId, String userId) {
        super(topic, clientId, userId);
    }

    @Override
    public Result authorize() {
        String id = topic.split("/")[1];
        if (userId == null) {//not logged in ==> not allowed to publish
            return Result.REJECT;
        }
        if (userId.equals(id)) {
            return Result.AUTHORIZE;
        } else {
            //You cannot publish presence of another person
            return Result.REJECT;
        }
    }
}

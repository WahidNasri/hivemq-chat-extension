package com.chat.authorizers.pubsub.publishAuthorizers;

import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;

public class ArchivePublishAuthorizer extends BaseChatAuthorize {
    public ArchivePublishAuthorizer(String topic, String clientId, String userId) {
        super(topic, clientId, userId);
    }

    @Override
    public Result authorize() {
        if(clientId.startsWith("supreme_")){
            return Result.AUTHORIZE;
        }
        else {
            return Result.REJECT;
        }
    }
}

package com.chat.authorizers.pubsub.subscribeAuthorizers;

import com.db.MyBatis;
import com.utils.JsonParser;
import com.client.ChatClient;
import com.db.ContactChat;
import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;

import java.util.List;

public class ArchiveRoomsSubscribeAuthorizer extends BaseChatAuthorize {
    public ArchiveRoomsSubscribeAuthorizer(String topic, String clientId, String userId) {
        super(topic, clientId, userId);
    }

    public Result authorize() {
        String foundClientId = topic.split("/")[1];
        if (foundClientId.equals(clientId) || userId.equals(foundClientId)) {
            return Result.AUTHORIZE;
        } else {
            return Result.REJECT;
        }
    }
}

package com.chat.authorizers.pubsub.subscribeAuthorizers;

import com.db.MyBatis;
import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;

import java.util.LinkedList;
import java.util.List;

public class PresenceSubscribeAuthorizer extends BaseChatAuthorize {
    public PresenceSubscribeAuthorizer(String topic, String clientId, String userId) {
        super(topic, clientId, userId);
    }

    @Override
    public Result authorize() {
        String id = topic.split("/")[1];
        if (userId == null) {//not logged in ==> not allowed to publish
            return Result.REJECT;
        }
        //Allow only contacts of id to subscribe
        List<String> userIds = new LinkedList<>();
        userIds.add(userId);
        userIds.add(id);
        String foundRoomId = MyBatis.getRoomIdThatContainUsers(userIds);

        if (foundRoomId != null) {
            return Result.AUTHORIZE;
        } else {
            return Result.REJECT;
        }
    }
}

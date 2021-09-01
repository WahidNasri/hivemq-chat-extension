package com.chat.authorizers.pubsub.publishAuthorizers;

import com.db.MyBatis;
import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;

public class MessagesPublishAuthorizer extends BaseChatAuthorize {
    public MessagesPublishAuthorizer(String topic, String clientId, String userId) {
        super(topic, clientId, userId);
    }

    @Override
    public Result authorize() {
        String roomId = topic.split("/")[1];
        if (userId == null) {//not logged in ==> not allowed to publish
            return Result.REJECT;
        }
        boolean isUserMember = MyBatis.isUSerMemberOfRoom(userId, roomId);
        //Allow publish only for members of the room
        if (isUserMember) {
            return Result.AUTHORIZE;
        } else {
            return Result.REJECT;
        }
    }
}
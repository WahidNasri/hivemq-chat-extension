package com.chat.authorizers.pubsub.subscribeAuthorizers;

import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;

public class InvitationSubscribeAuthorizer extends BaseChatAuthorize {
    public InvitationSubscribeAuthorizer(String topic, String clientId, String userId) {
        super(topic, clientId, userId);
    }

    @Override
    public Result authorize() {
        //reject all subscriptions to "invitations/", the invitations will be send by supreme user to personalevents/userId
        return Result.REJECT;
    }
}

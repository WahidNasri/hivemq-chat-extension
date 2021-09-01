package com.chat.authorizers.pubsub.subscribeAuthorizers;

import com.db.MyBatis;
import com.utils.JsonParser;
import com.client.ChatClient;
import com.db.ContactChat;
import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;

public class ArchiveIdSubscribeAuthorizer extends BaseChatAuthorize {
    public ArchiveIdSubscribeAuthorizer(String topic, String clientId, String userId) {
        super(topic, clientId, userId);
    }

    @Override
    public Result authorize() {
        String foundClientId = topic.split("/")[1];
        if(foundClientId.equals(clientId)) {
            new Thread(()->{
                //*
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                 //*/
                ContactChat user = MyBatis.getUserByID(userId);
                String payload = JsonParser.toJson(user);
                ChatClient.connectAndPublish(payload, topic);
            }).start();

            return Result.AUTHORIZE;
        }
        else {
            return Result.REJECT;
        }
    }
}

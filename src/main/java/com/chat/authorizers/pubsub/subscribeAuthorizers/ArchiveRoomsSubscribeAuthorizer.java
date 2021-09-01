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
        if (foundClientId.equals(clientId)) {
            Thread publish = new Thread(() -> {
                //fetch groups and contacts then publish them to the topic
                //*
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //*/
                List<ContactChat> chats = MyBatis.getUserContacts(userId);
                String payload = JsonParser.toJson(chats);

                ChatClient.connectAndPublish(payload, topic);
            });
            publish.start();
            return Result.AUTHORIZE;
        } else {
            return Result.REJECT;
        }
    }
}

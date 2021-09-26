package com.chat.authorizers.pubsub.publishAuthorizers;

import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;
import com.client.ChatClient;
import com.db.ContactChat;
import com.db.MyBatis;
import com.db.Room;
import com.models.BaseMessage;
import com.models.MessageType;
import com.utils.JsonParser;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PersonalEventsPublishAuthorizer extends BaseChatAuthorize {
    String payload;

    public PersonalEventsPublishAuthorizer(String topic, String clientId, String userId, String payload) {
        super(topic, clientId, userId);
        this.payload = payload;
    }

    @Override
    public Result authorize() {
        String receiverId = topic.split("/")[1];

        BaseMessage bm = JsonParser.fromJson(payload, BaseMessage.class);
        if (bm.getType() == MessageType.EventInvitationResponseAccept) {
            //update invitation state
            MyBatis.updateInvitationState(bm.getId(), "accepted");
            //create room and membership, then send contacts to each member.
            Room room = new Room();
            room.setId(UUID.randomUUID().toString());

            List<String> userIds = new LinkedList<>();
            userIds.add(userId);
            userIds.add(receiverId);

            MyBatis.createRoomWithMembers(room, userIds);



            new Thread(()->{
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<ContactChat> receiverContacts = MyBatis.getUserContacts(receiverId);
                List<ContactChat> userContacts = MyBatis.getUserContacts(userId);
                ChatClient.connectAndPublish(JsonParser.toJson(receiverContacts), "archivesrooms/" + receiverId);
                ChatClient.connectAndPublish(JsonParser.toJson(userContacts), "archivesrooms/" + userId);
            }).start();

        } else if (bm.getType() == MessageType.EventInvitationResponseReject) {
            //update invitation state
            MyBatis.updateInvitationState(bm.getId(), "rejected");
        }

        return Result.AUTHORIZE;
    }
}

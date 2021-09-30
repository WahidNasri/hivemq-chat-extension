package com.chat.authorizers.pubsub.publishAuthorizers;

import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;
import com.client.ChatClient;
import com.db.ContactChat;
import com.db.MyBatis;
import com.db.Room;
import com.models.BaseMessage;
import com.models.ChatMessage;
import com.models.MessageType;
import com.utils.JsonParser;

import java.util.LinkedList;
import java.util.List;

public class MucPublishAuthorizer extends BaseChatAuthorize {
    private String payload;
    public MucPublishAuthorizer(String topic, String clientId, String userId, String payload) {
        super(topic, clientId, userId);
        this.payload = payload;
    }

    @Override
    public Result authorize() {
        String groupId = topic.split("/")[1];
        try{
            ChatMessage bm = JsonParser.fromJson(payload, ChatMessage.class);
            if(bm.getType() == MessageType.CreateGroup){
                createGroup(groupId, bm.getText(), bm.getAdditionalFields(), userId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.AUTHORIZE;
    }

    private void createGroup(String id, String name, List<String> membersIds, String adminId) {
        Room room = new Room();
        room.setId(id);
        room.setGroup(true);
        room.setName(name);

        membersIds.add(adminId);

        MyBatis.createRoomWithMembers(room, membersIds);


        new Thread(() -> {
            //=============
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //==============

            ContactChat chat = new ContactChat();
            chat.setGroup(true);
            chat.setId(id);
            chat.setFirstName(name);
            chat.setLastName("");
            chat.setRoomId(id);

            List<ContactChat> list = new LinkedList<>();
            list.add(chat);

            for(String memberId : membersIds) {
                ChatClient.connectAndPublish(JsonParser.toJson(list), "archivesrooms/" + memberId);
            }
        }).start();
    }
}

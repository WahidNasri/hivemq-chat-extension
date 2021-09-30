package com.chat.authorizers.pubsub.subscribeAuthorizers;

import com.db.MyBatis;
import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;
import com.db.Room;

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
        //if the id is a Group Id, then authorize if the user is a member
        Room room = MyBatis.getRoomById(id);
        if(room != null && room.isGroup()){
            //fixme: check if the user is a member
            return Result.AUTHORIZE;
        }
        else{
            //assuming the is id of a user id
            //Allow only contacts of id to subscribe
            List<String> userIds = new LinkedList<>();
            userIds.add(userId);
            userIds.add(id);

            List<String> foundRoomsId = MyBatis.getRoomsIdThatContainUsers(userIds);
            boolean isContact = false;
            for(String roomId : foundRoomsId){
                Room foundRoom = MyBatis.getRoomById(roomId);
                //authorize only if the room is not a group.
                isContact = foundRoom != null && !foundRoom.isGroup();
                if(isContact){
                    break;
                }
            }
            if (isContact) {
                return Result.AUTHORIZE;
            } else {
                return Result.REJECT;
            }
        }

    }
}

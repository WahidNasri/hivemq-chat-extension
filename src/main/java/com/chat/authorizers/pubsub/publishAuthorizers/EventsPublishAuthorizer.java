package com.chat.authorizers.pubsub.publishAuthorizers;

import com.db.MyBatis;
import com.utils.JsonParser;
import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;
import com.models.BaseMessage;
import com.models.ChatMarkerMessage;
import com.models.MessageType;

public class EventsPublishAuthorizer extends BaseChatAuthorize {
    private String payload;
    public EventsPublishAuthorizer(String topic, String clientId, String userId, String payload) {
        super(topic, clientId, userId);
        this.payload = payload;
    }

    @Override
    public Result authorize() {
        String roomId = topic.split("/")[1];
        if (userId == null) {//not logged in ==> not allowed to publish
            return Result.REJECT;
        }
        boolean isUserMember = MyBatis.isUSerMemberOfRoom(userId, roomId);

        //prevent non members to publish
        if (!isUserMember) {
            return Result.REJECT;
        }
        try {
            BaseMessage baseMessage = JsonParser.fromJson(payload, BaseMessage.class);

            //If chat marker, do not authorize publish for message author
            if (baseMessage.getType() == MessageType.ChatMarker) {
                ChatMarkerMessage cmm = JsonParser.fromJson(payload, ChatMarkerMessage.class);
                if (cmm.getReferenceId() != null) {
                    String authorId = MyBatis.getMessageAuthorId(cmm.getReferenceId());
                    if (userId.equals(authorId)) {
                        //You cannot chat mark your own message
                        return Result.REJECT;
                    }
                    else{
                        return Result.AUTHORIZE;
                    }
                } else {
                    //No authorize chat Marker without reference id
                    return Result.REJECT;
                }
            } else {
                return Result.AUTHORIZE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //in case of error, let other extension decide or use default

            return Result.NEXT;
        }
    }
}

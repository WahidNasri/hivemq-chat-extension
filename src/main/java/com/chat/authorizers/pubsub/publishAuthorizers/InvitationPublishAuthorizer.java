package com.chat.authorizers.pubsub.publishAuthorizers;

import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;
import com.client.ChatClient;
import com.db.ContactChat;
import com.db.Invitation;
import com.db.MyBatis;
import com.models.InvitationMessage;
import com.models.InvitationMessageType;
import com.utils.JsonParser;

import java.util.UUID;

public class InvitationPublishAuthorizer extends BaseChatAuthorize {
    private String payload;
    public InvitationPublishAuthorizer(String topic, String clientId, String userId, String payload) {
        super(topic, clientId, userId);
        this.payload = payload;
    }

    @Override
    public Result authorize() {
        String inviteeUsername = topic.split("/")[1];
        InvitationMessage payloadMessage = JsonParser.fromJson(payload, InvitationMessage.class);
        if(userId == null){
            //reject not logged in users
            return Result.REJECT;
        }
        String inviteeUserId = MyBatis.getUserIdByUsername(inviteeUsername);
        if(inviteeUserId == null){
            //invitee not found ==> reject
            String pl = JsonParser.toJson(InvitationMessage.createError("User not found", payloadMessage.getId()));
            ChatClient.connectAndPublish(pl, "personalevents/" + userId);

            return Result.AUTHORIZE;
        }
        if(inviteeUserId.equals(userId)){
            String pl = JsonParser.toJson(InvitationMessage.createError("Cannot sent invitation to yourself", payloadMessage.getId()));
            ChatClient.connectAndPublish(pl, "personalevents/" + userId);

            return Result.AUTHORIZE;

        }
        Invitation oldInvitation = MyBatis.getIngoingInvitation(userId, inviteeUserId);
        if(oldInvitation != null){
            //there is already an ingoing invitation ==> reject
            ChatClient.connectAndPublish(JsonParser.toJson(InvitationMessage.createError("there is already an ingoing invitation", payloadMessage.getId())), "personalevents/" + userId);

            return Result.AUTHORIZE;
        }
        //TODO: implement this on SQL
        boolean isContact = false;
        for(ContactChat contactChat :MyBatis.getUserContacts(userId)){
            if(contactChat.getId().equals(inviteeUserId)){
                isContact = true;
                break;
            }
        }

        if(isContact){
            //user is already a contact ==> reject
            ChatClient.connectAndPublish(JsonParser.toJson(InvitationMessage.createError("User is already a contact", payloadMessage.getId())), "personalevents/" + userId);
            return Result.AUTHORIZE;
        }

        //================ SEND INVITATION PAYLOAD==================//
        //InvitationMessage invitationMessage = InvitationMessage.createNew(userId, payloadMessage.getId());


        String topic = "personalevents/" + inviteeUserId;
        //send invitation to the invitee
        ChatClient.connectAndPublish(JsonParser.toJson(payloadMessage), topic);

        //send confirmation to the inviter
        ChatClient.connectAndPublish(JsonParser.toJson(InvitationMessage.createInfo("Invitation sent", payloadMessage.getId())), "personalevents/" + userId

        );
        //================ INSERT INVITATION=====================
        Invitation invitation = new Invitation();
        invitation.setFrom_id(userId);
        invitation.setTo_id(inviteeUserId);
        invitation.setId(payloadMessage.getId());
        MyBatis.insertInvitation(invitation);


        return Result.AUTHORIZE;
    }
}

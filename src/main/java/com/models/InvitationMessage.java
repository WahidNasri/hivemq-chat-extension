package com.models;


import java.util.UUID;

public class InvitationMessage {
    private String id;
    private MessageType type;
    private String fromId;
    private String fromName;
    private String fromAvatar;
    private long sendTime;
    private String text;
    private InvitationMessageType invitationMessageType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public InvitationMessageType getInvitationMessageType() {
        return invitationMessageType;
    }

    public void setInvitationMessageType(InvitationMessageType invitationMessageType) {
        this.invitationMessageType = invitationMessageType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public String getFromAvatar() {
        return fromAvatar;
    }

    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }

    public static InvitationMessage createNew(String fromId, String id){
        InvitationMessage invitationMessage = new InvitationMessage();
        invitationMessage.setFromId(fromId);
        invitationMessage.setType(MessageType.EventInvitationRequest);
        invitationMessage.setInvitationMessageType(InvitationMessageType.REQUEST_RESPONSE);
        invitationMessage.setId(id != null ? id : UUID.randomUUID().toString());

        return invitationMessage;
    }
    public static InvitationMessage createError(String error, String id){
        InvitationMessage invitationMessage = new InvitationMessage();
        invitationMessage.setFromId("supreme_");
        invitationMessage.setType(MessageType.EventInvitationResponseReject);

        invitationMessage.setInvitationMessageType(InvitationMessageType.ERROR);
        invitationMessage.setId(id);
        invitationMessage.setText(error);

        return invitationMessage;
    }
    public static InvitationMessage createInfo(String info, String id){
        InvitationMessage invitationMessage = new InvitationMessage();
        invitationMessage.setFromId("supreme_");
        invitationMessage.setType(MessageType.EventInvitationResponseReject);

        invitationMessage.setInvitationMessageType(InvitationMessageType.INFO);
        invitationMessage.setId(id);
        invitationMessage.setText(info);

        return invitationMessage;
    }
}

package com.chat.authorizers;

public abstract class BaseChatAuthorize {
    protected String topic, clientId, userId;
    public BaseChatAuthorize(String topic, String clientId, String userId){
        this.topic = topic;
        this.clientId = clientId;
        this.userId = userId;
    }
    public abstract Result authorize();
}

package com.db;

import java.util.Map;

public class Message {
    private String id;
    private String type;
    private String from_id;
    private String text;
    private String originality;
    private String attachment;
    private String thumbnail;
    private String original_id;
    private String room;
    private long send_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOriginal_id() {
        return original_id;
    }

    public void setOriginal_id(String original_id) {
        this.original_id = original_id;
    }

    public String getOriginality() {
        return originality;
    }

    public void setOriginality(String originality) {
        this.originality = originality;
    }

    public long getSend_time() {
        return send_time;
    }

    public void setSend_time(long send_time) {
        this.send_time = send_time;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public static Message fromClientMap(Map<String, Object> map){
        Message message = new Message();

        message.setId(map.get("id").toString());
        message.setType(map.get("type").toString());
        message.setFrom_id(map.get("fromId").toString());
        message.setText(map.get("text").toString());
        message.setOriginality(map.get("originality").toString());
        if(map.get("attachment") != null) {
            message.setAttachment(map.get("attachment").toString());
        }
        if(map.get("thumbnail") != null) {
            message.setThumbnail(map.get("thumbnail").toString());
        }
        if(map.get("originalId") != null) {
            message.setOriginal_id(map.get("originalId").toString());
        }
        message.setSend_time(Long.parseLong(map.get("sendTime").toString()));
        message.setRoom(map.get("roomId").toString());

        return message;
    }
}

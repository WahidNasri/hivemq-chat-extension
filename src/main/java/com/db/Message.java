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

    public static Message fromClientMap(Map<String, String> map){
        Message message = new Message();

        message.setId(map.get("id"));
        message.setType(map.get("type"));
        message.setFrom_id(map.get("fromId"));
        message.setText(map.get("text"));
        message.setOriginality(map.get("originality"));
        message.setAttachment(map.get("attachment"));
        message.setThumbnail(map.get("thumbnail"));
        message.setOriginal_id(map.get("originalId"));
        message.setSend_time(Long.parseLong(map.get("sendTime")));
        message.setRoom(map.get("roomId"));

        return message;
    }
}

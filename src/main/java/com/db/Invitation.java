package com.db;

public class Invitation {
    private String id;
    private String from_id;
    private String to_id;
    private String state;
    private int sent_date;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getSent_date() {
        return sent_date;
    }

    public void setSent_date(int sent_date) {
        this.sent_date = sent_date;
    }
}

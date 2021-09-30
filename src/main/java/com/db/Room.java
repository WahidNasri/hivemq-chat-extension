package com.db;

public class Room {
    private String id;
    private String name;
    private String avatar;
    private boolean is_group;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isGroup() {
        return is_group;
    }

    public void setGroup(boolean group) {
        is_group = group;
    }
}

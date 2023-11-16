package model;

import helper.DateManager;

import java.util.UUID;

public class Comment {

    private String id;
    private String text;
    private String userid;
    private User user;
    private final String createdAt;

    public Comment(String id, String text, String userid, User user, String createdAt) {
        this.id = id;
        this.text = text;
        this.userid = userid;
        this.user = user;
        this.createdAt = createdAt;
    }

    public Comment(String text, String userid) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.userid = userid;
        this.createdAt = DateManager.getNow();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}

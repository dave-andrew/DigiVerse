package model;

import helper.DateManager;

import java.util.UUID;

public class Forum {

    private String id;
    private String text;
    private String userid;
    private User user;
    private String classid;
    private Classroom classroom;
    private String createdAt;

    public Forum(String id, String text, String userid, User user, String classid, Classroom classroom, String createdAt) {
        this.id = id;
        this.text = text;
        this.userid = userid;
        this.user = user;
        this.classid = classid;
        this.classroom = classroom;
        this.createdAt = createdAt;
    }

    public Forum(String text, String userid, String classid) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.userid = userid;
        this.classid = classid;
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

    public void setUser(User user) {
        this.user = user;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

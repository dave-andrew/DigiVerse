package net.slc.dv.model;

import net.slc.dv.helper.DateManager;

import java.util.UUID;

public class Task {

    private String id;
    private String userid;
    private User user;
    private String title;
    private String description;
    private String deadlineAt;
    private String createdAt;
    private boolean scored;

    private Classroom classroom;

    public Task(String id, String userid, User user, String title, String description, String deadlineAt, String createdAt, boolean scored) {
        this.id = id;
        this.userid = userid;
        this.user = user;
        this.title = title;
        this.description = description;
        this.deadlineAt = deadlineAt;
        this.createdAt = createdAt;
        this.scored = scored;
    }

    public Task(String userid, User user, String title, String description, String deadlineAt, boolean scored) {
        this.id = UUID.randomUUID().toString();
        this.userid = userid;
        this.user = user;
        this.title = title;
        this.description = description;
        this.deadlineAt = deadlineAt;
        this.createdAt = DateManager.getNow();
        this.scored = scored;
    }

    public Task(String id, String userid, User user, String title, String description, String deadlineAt, String createdAt, boolean scored, Classroom classroom) {
        this.id = id;
        this.userid = userid;
        this.user = user;
        this.title = title;
        this.description = description;
        this.deadlineAt = deadlineAt;
        this.createdAt = createdAt;
        this.scored = scored;
        this.classroom = classroom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadlineAt() {
        return deadlineAt;
    }

    public void setDeadlineAt(String deadlineAt) {
        this.deadlineAt = deadlineAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isScored() {
        return scored;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }
}

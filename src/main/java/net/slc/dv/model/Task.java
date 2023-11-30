package net.slc.dv.model;

import lombok.Getter;
import lombok.Setter;
import net.slc.dv.helper.DateManager;

import java.util.UUID;

@Getter
@Setter
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

}

package net.slc.dv.model;

import lombok.Getter;
import lombok.Setter;
import net.slc.dv.helper.DateManager;

import java.util.UUID;

@Getter
@Setter
public class Forum {

    private String id;
    private String text;
    private String userid;
    private User user;
    private String classid;
    private Classroom classroom;
    private String createdAt;

    private int commentCounter;
    private boolean toggle;

    public Forum(String id, String text, String userid, User user, String classid, Classroom classroom, String createdAt) {
        this.id = id;
        this.text = text;
        this.userid = userid;
        this.user = user;
        this.classid = classid;
        this.classroom = classroom;
        this.createdAt = createdAt;
        this.commentCounter = 0;
        this.toggle = true;
    }

    public Forum(String text, String userid, String classid) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.userid = userid;
        this.classid = classid;
        this.createdAt = DateManager.getNow();
        this.commentCounter = 0;
        this.toggle = true;
    }

}

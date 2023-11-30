package net.slc.dv.model;

import lombok.Getter;
import lombok.Setter;
import net.slc.dv.helper.DateManager;

import java.util.UUID;

@Getter
@Setter
public class Comment {

    private final User user;
    private final String createdAt;
    private String id;
    private String replyid;
    private String text;
    private String userid;

    public Comment(String id, String text, String userid, User user, String createdAt) {
        this.id = id;
        this.text = text;
        this.userid = userid;
        this.user = user;
        this.createdAt = createdAt;
    }

    public Comment(String text, String userid, User user) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.userid = userid;
        this.user = user;
        this.createdAt = DateManager.getNow();
    }

    public Comment(String replyid, String text, String userid, User user) {
        this.id = UUID.randomUUID().toString();
        this.replyid = replyid;
        this.text = text;
        this.userid = userid;
        this.user = user;
        this.createdAt = DateManager.getNow();
    }

}

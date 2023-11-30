package net.slc.dv.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForumComment extends Comment {

    private String forumid;
    private Forum forum;

    public ForumComment(String id, String text, String userid, User user, String createdAt, String forumid, Forum forum) {
        super(id, text, userid, user, createdAt);

        this.forumid = forumid;
        this.forum = forum;
    }

    public ForumComment(String text, String userid, User user, String forumid) {
        super(text, userid, user);
        this.forumid = forumid;
    }

}

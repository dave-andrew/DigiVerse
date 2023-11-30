package net.slc.dv.model;

public class ForumComment extends Comment{

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


    public String getForumid() {
        return forumid;
    }

    public void setForumid(String forumid) {
        this.forumid = forumid;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }
}

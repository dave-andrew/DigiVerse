package controller;

import database.CommentQuery;
import model.Comment;
import model.ForumComment;

public class CommentController {

    private CommentQuery commentQuery;

    public CommentController() {
        this.commentQuery = new CommentQuery();
    }

    public ForumComment createForumComment(String text, String forumid, String userid) {

        ForumComment forumComment = new ForumComment(text, userid, forumid);

        return this.commentQuery.createForumComment(forumComment);
    }

}

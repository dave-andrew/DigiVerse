package controller;

import database.CommentQuery;
import model.Comment;
import model.ForumComment;
import model.LoggedUser;
import model.TaskComment;

import java.util.ArrayList;

public class CommentController {

    private CommentQuery commentQuery;

    public CommentController() {
        this.commentQuery = new CommentQuery();
    }

    public ForumComment createForumComment(String text, String forumid, String userid) {

        ForumComment forumComment = new ForumComment(text, userid, LoggedUser.getInstance(), forumid);

        return this.commentQuery.createForumComment(forumComment);
    }

    public ArrayList<ForumComment> getForumComments(String forumid, int offset) {
        return this.commentQuery.getForumComments(forumid, offset);
    }

    public TaskComment createTaskComment(String text, String taskid, String userid) {

        if(text == null || text.isEmpty()) return null;

        TaskComment taskComment = new TaskComment(text, userid, LoggedUser.getInstance(), taskid);

        return this.commentQuery.createTaskComment(taskComment);
    }

    public ArrayList<TaskComment> getTaskComments(String taskid) {
        return this.commentQuery.getTaskComments(taskid);
    }

    public TaskComment replyComment(String commentid, String text) {
        return null;
    }

}

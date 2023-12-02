package net.slc.dv.database;

import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.database.connection.Connect;
import net.slc.dv.helper.Closer;
import net.slc.dv.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommentQuery {

    private final Connect connect;

    public CommentQuery() {
        this.connect = Connect.getConnection();
    }

    public ForumComment createForumComment(ForumComment forumComment) {
        createComment(forumComment.getId(), forumComment.getText(), forumComment.getUserid(), forumComment.getCreatedAt());

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("forum_comment")
                    .values("ForumID", forumComment.getForumid())
                    .values("CommentID", forumComment.getId());

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return forumComment;
    }

    private void createComment(String id, String text, String userid, String createdAt) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("mscomment")
                    .values("CommentID", id)
                    .values("ReplyID", null)
                    .values("CommentText", text)
                    .values("UserID", userid)
                    .values("CreatedAt", createdAt);

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ForumComment> getForumComments(String forumId, int offset) {
        ArrayList<ForumComment> forumCommentList = new ArrayList<>();

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("forum_comment")
                    .join("forum_comment", "CommentID", "mscomment", "CommentID")
                    .join("mscomment", "UserID", "msuser", "UserID")
                    .join("forum_comment", "ForumID", "msforum", "ForumID")
                    .condition("forum_comment.ForumID", "=", forumId)
                    .orderBy("mscomment.CreatedAt", "DESC")
                    .limit(5)
                    .offset(offset * 5);

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            while (set.next()) {
                User user = new User(
                        set.getString("UserID"),
                        set.getString("Username"),
                        set.getString("UserEmail"),
                        "",
                        set.getString("UserDOB"),
                        set.getBlob("UserProfile"));

                Forum forum = new Forum(
                        set.getString("ForumID"),
                        set.getString("ForumText"),
                        set.getString("UserID"),
                        user,
                        "",
                        null,
                        set.getString("CreatedAt"));

                ForumComment forumComment = new ForumComment(
                        set.getString("CommentID"),
                        set.getString("CommentText"),
                        set.getString("UserID"),
                        user,
                        set.getString("CreatedAt"),
                        set.getString("ForumID"),
                        forum
                );

                forumCommentList.add(forumComment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return forumCommentList;
    }

    public TaskComment createTaskComment(TaskComment taskComment) {
        createComment(taskComment.getId(), taskComment.getText(), taskComment.getUserid(), taskComment.getCreatedAt());

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("task_comment")
                    .values("TaskID", taskComment.getTaskid())
                    .values("CommentID", taskComment.getId());

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taskComment;
    }

    public ArrayList<TaskComment> getTaskComments(String taskid) {
        ArrayList<TaskComment> taskList = new ArrayList<>();

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("task_comment")
                    .join("task_comment", "CommentID", "mscomment", "CommentID")
                    .join("mscomment", "UserID", "msuser", "UserID")
                    .join("task_comment", "TaskID", "mstask", "TaskID")
                    .condition("task_comment.TaskID", "=", taskid)
                    .orderBy("mscomment.CreatedAt", "DESC");

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            while (set.next()) {
                User user = new User(
                        set.getString("UserID"),
                        set.getString("Username"),
                        set.getString("UserEmail"),
                        "",
                        set.getString("UserDOB"),
                        set.getBlob("UserProfile"));

                Task task = new Task(set, user);

                TaskComment taskComment = new TaskComment(
                        set.getString("CommentID"),
                        set.getString("CommentText"),
                        set.getString("UserID"),
                        user,
                        set.getString("CreatedAt"),
                        set.getString("TaskID"),
                        task
                );

                taskList.add(taskComment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taskList;
    }

    public ArrayList<TaskComment> getStudentTaskComments(String taskid) {
        ArrayList<TaskComment> taskList = new ArrayList<>();

        String query = "SELECT * FROM task_comment\n" +
                "JOIN mscomment ON mscomment.CommentID = task_comment.CommentID\n" +
                "JOIN msuser ON msuser.UserID = mscomment.UserID\n" +
                "JOIN mstask ON mstask.TaskID = task_comment.TaskID\n" +
                "WHERE task_comment.TaskID = ?\n" +
                "AND (mscomment.UserID IN (" +
                "SELECT UserID FROM class_task\n" +
                "JOIN msclass ON msclass.ClassID = class_task.ClassID\n" +
                "JOIN class_member ON msclass.ClassID = class_member.ClassID\n" +
                "WHERE Role = 'Teacher' AND class_task.TaskID = ?" +
                ") OR mscomment.UserID = ?)\n" +
                "ORDER BY mscomment.CreatedAt DESC";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, taskid);
            ps.setString(2, taskid);
            ps.setString(3, LoggedUser.getInstance().getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    User user = new User(rs);

                    Task task = new Task(rs, user);

                    TaskComment taskComment = new TaskComment(
                            rs.getString("CommentID"),
                            rs.getString("CommentText"),
                            rs.getString("UserID"),
                            user,
                            rs.getString("CreatedAt"),
                            rs.getString("TaskID"),
                            task
                    );

                    taskList.add(taskComment);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return taskList;
    }

    public TaskComment replyComment(TaskComment replyComment) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("mscomment")
                    .values("CommentID", replyComment.getId())
                    .values("ReplyID", replyComment.getReplyid())
                    .values("CommentText", replyComment.getText())
                    .values("UserID", replyComment.getUserid())
                    .values("CreatedAt", replyComment.getCreatedAt());

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return replyComment;
    }

    public ArrayList<TaskComment> getReplyTaskComment(String commentid) {
        ArrayList<TaskComment> taskList = new ArrayList<>();

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("mscomment")
                    .join("mscomment", "UserID", "msuser", "UserID")
                    .condition("mscomment.ReplyID", "=", commentid)
                    .orderBy("mscomment.CreatedAt", "DESC");

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            while (set.next()) {
                User user = new User(
                        set.getString("UserID"),
                        set.getString("Username"),
                        set.getString("UserEmail"),
                        "",
                        set.getString("UserDOB"),
                        set.getBlob("UserProfile"));

                TaskComment replyTaskComment = new TaskComment(
                        set.getString("CommentID"),
                        set.getString("CommentText"),
                        set.getString("UserID"),
                        user,
                        set.getString("CreatedAt"),
                        set.getString("ReplyID"),
                        null
                );

                taskList.add(replyTaskComment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taskList;
    }

}

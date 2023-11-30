package database;

import database.connection.Connect;
import model.*;

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
        String query = "INSERT INTO mscomment VALUES (?, NULL, ?, ?, ?)";
        String query2 = "INSERT INTO forum_comment VALUES (?, ?)";

        try (PreparedStatement ps = connect.prepareStatement(query);
             PreparedStatement ps2 = connect.prepareStatement(query2)) {
            assert ps != null;

            ps.setString(1, forumComment.getId());
            ps.setString(2, forumComment.getText());
            ps.setString(3, forumComment.getUserid());
            ps.setString(4, forumComment.getCreatedAt());

            ps.executeUpdate();

            assert ps2 != null;
            ps2.setString(1, forumComment.getForumid());
            ps2.setString(2, forumComment.getId());

            ps2.executeUpdate();

            return forumComment;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ForumComment> getForumComments(String forumId, int offset) {
        ArrayList<ForumComment> forumCommentList = new ArrayList<>();

        String query = "SELECT * FROM forum_comment\n" +
                "JOIN mscomment ON mscomment.CommentID = forum_comment.CommentID\n" +
                "JOIN msuser ON msuser.UserID = mscomment.UserID\n" +
                "JOIN msforum ON msforum.ForumID = forum_comment.ForumID\n" +
                "WHERE forum_comment.ForumID = ?\n" +
                "ORDER BY mscomment.CreatedAt DESC\n" +
                "LIMIT 5 OFFSET ?";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, forumId);
            ps.setInt(2, offset * 5);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    User user = new User(
                            rs.getString("UserID"),
                            rs.getString("Username"),
                            rs.getString("UserEmail"),
                            "",
                            rs.getString("UserDOB"),
                            rs.getBlob("UserProfile"));

                    Forum forum = new Forum(
                            rs.getString("ForumID"),
                            rs.getString("ForumText"),
                            rs.getString("UserID"),
                            user,
                            "",
                            null,
                            rs.getString("CreatedAt"));

                    ForumComment forumComment = new ForumComment(
                            rs.getString("CommentID"),
                            rs.getString("CommentText"),
                            rs.getString("UserID"),
                            user,
                            rs.getString("CreatedAt"),
                            rs.getString("ForumID"),
                            forum
                    );

                    forumCommentList.add(forumComment);
                }
            }

            return forumCommentList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TaskComment createTaskComment(TaskComment taskComment) {
        String query = "INSERT INTO mscomment VALUES (?, NULL, ?, ?, ?)";
        String query2 = "INSERT INTO task_comment VALUES (?, ?)";

        try (PreparedStatement ps = connect.prepareStatement(query);
             PreparedStatement ps2 = connect.prepareStatement(query2)) {

            assert ps != null;
            ps.setString(1, taskComment.getId());
            ps.setString(2, taskComment.getText());
            ps.setString(3, taskComment.getUserid());
            ps.setString(4, taskComment.getCreatedAt());

            ps.executeUpdate();

            assert ps2 != null;
            ps2.setString(1, taskComment.getTaskid());
            ps2.setString(2, taskComment.getId());

            ps2.executeUpdate();

            return taskComment;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<TaskComment> getTaskComments(String taskid) {
        ArrayList<TaskComment> taskList = new ArrayList<>();

        String query = "SELECT * FROM task_comment\n" +
                "JOIN mscomment ON mscomment.CommentID = task_comment.CommentID\n" +
                "JOIN msuser ON msuser.UserID = mscomment.UserID\n" +
                "JOIN mstask ON mstask.TaskID = task_comment.TaskID\n" +
                "WHERE task_comment.TaskID = ?\n" +
                "ORDER BY mscomment.CreatedAt DESC";

        try (PreparedStatement ps = connect.prepareStatement(query)) {

            assert ps != null;
            ps.setString(1, taskid);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getString("UserID"),
                            rs.getString("Username"),
                            rs.getString("UserEmail"),
                            "",
                            rs.getString("UserDOB"),
                            rs.getBlob("UserProfile"));

                    Task task = new Task(
                            rs.getString("TaskID"),
                            rs.getString("UserID"),
                            user,
                            rs.getString("TaskTitle"),
                            rs.getString("TaskDesc"),
                            rs.getString("DeadlineAt"),
                            rs.getString("CreatedAt"),
                            rs.getBoolean("Scored")
                    );

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

        System.out.println(taskList);

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

                    User user = new User(
                            rs.getString("UserID"),
                            rs.getString("Username"),
                            rs.getString("UserEmail"),
                            "",
                            rs.getString("UserDOB"),
                            rs.getBlob("UserProfile"));

                    Task task = new Task(
                            rs.getString("TaskID"),
                            rs.getString("UserID"),
                            user,
                            rs.getString("TaskTitle"),
                            rs.getString("TaskDesc"),
                            rs.getString("DeadlineAt"),
                            rs.getString("CreatedAt"),
                            rs.getBoolean("Scored")
                    );

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
        String query = "INSERT INTO mscomment VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, replyComment.getId());
            ps.setString(2, replyComment.getReplyid());
            ps.setString(3, replyComment.getText());
            ps.setString(4, replyComment.getUserid());
            ps.setString(5, replyComment.getCreatedAt());

            ps.executeUpdate();

            return replyComment;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<TaskComment> getReplyTaskComment(String commentid) {
        ArrayList<TaskComment> taskList = new ArrayList<>();

        String query = "SELECT * FROM mscomment\n" +
                "JOIN msuser ON msuser.UserID = mscomment.UserID\n" +
                "WHERE mscomment.ReplyID = ?\n" +
                "ORDER BY mscomment.CreatedAt DESC";

        try (PreparedStatement ps = connect.prepareStatement(query)) {

            assert ps != null;
            ps.setString(1, commentid);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {

                    User user = new User(
                            rs.getString("UserID"),
                            rs.getString("Username"),
                            rs.getString("UserEmail"),
                            "",
                            rs.getString("UserDOB"),
                            rs.getBlob("UserProfile"));

                    TaskComment replyTaskComment = new TaskComment(
                            rs.getString("CommentID"),
                            rs.getString("CommentText"),
                            rs.getString("UserID"),
                            user,
                            rs.getString("CreatedAt"),
                            rs.getString("ReplyID"),
                            null
                    );

                    taskList.add(replyTaskComment);

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return taskList;
    }

}

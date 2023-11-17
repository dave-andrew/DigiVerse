package database;

import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommentQuery {

    private Connect con;

    public CommentQuery() {
        this.con = Connect.getConnection();
    }

    public ForumComment createForumComment(ForumComment forumComment) {

        String query = "INSERT INTO mscomment VALUES (?, NULL, ?, ?, ?)";
        String query2 = "INSERT INTO forum_comment VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query);
            PreparedStatement ps2 = con.prepareStatement(query2)) {

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

    public ArrayList<ForumComment> getForumComments(String forumid, int offset) {

        ArrayList<ForumComment> forumCommentList = new ArrayList<>();

        String query = "SELECT * FROM forum_comment\n" +
                "JOIN mscomment ON mscomment.CommentID = forum_comment.CommentID\n" +
                "JOIN msuser ON msuser.UserID = mscomment.UserID\n" +
                "JOIN msforum ON msforum.ForumID = forum_comment.ForumID\n" +
                "WHERE forum_comment.ForumID = ?\n" +
                "ORDER BY mscomment.CreatedAt DESC\n" +
                "LIMIT 5 OFFSET ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {

            assert ps != null;
            ps.setString(1, forumid);
            ps.setInt(2, offset * 5);

            try(var rs = ps.executeQuery()) {
                while(rs.next()) {

                    User user = new User(
                            rs.getString("UserID"),
                            rs.getString("Username"),
                            rs.getString("UserEmail"),
                            "",
                            rs.getInt("UserAge"),
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

        try (PreparedStatement ps = con.prepareStatement(query);
            PreparedStatement ps2 = con.prepareStatement(query2)) {

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

        try (PreparedStatement ps = con.prepareStatement(query)) {

            assert ps != null;
            ps.setString(1, taskid);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {

                    User user = new User(
                            rs.getString("UserID"),
                            rs.getString("Username"),
                            rs.getString("UserEmail"),
                            "",
                            rs.getInt("UserAge"),
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
                "AND (mscomment.UserID IN (SELECT UserID FROM class_member WHERE Role = 'Teacher') OR mscomment.UserID = ?)\n" +
                "ORDER BY mscomment.CreatedAt DESC";

        try (PreparedStatement ps = con.prepareStatement(query)) {

            assert ps != null;
            ps.setString(1, taskid);
            ps.setString(2, LoggedUser.getInstance().getId());

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {

                    User user = new User(
                            rs.getString("UserID"),
                            rs.getString("Username"),
                            rs.getString("UserEmail"),
                            "",
                            rs.getInt("UserAge"),
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

}

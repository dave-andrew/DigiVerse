package database;

import model.Classroom;
import model.Forum;
import model.ForumComment;
import model.User;

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

        String query = "INSERT INTO mscomment VALUES (?, NULL, ?)";
        String query2 = "INSERT INTO forum_comment VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query);
            PreparedStatement ps2 = con.prepareStatement(query2)) {

            assert ps != null;
            ps.setString(1, forumComment.getId());
            ps.setString(2, forumComment.getText());

            ps.executeUpdate();

            assert ps2 != null;
            ps2.setString(1, forumComment.getForumid());
            ps2.setString(2, forumComment.getId());
            ps2.setString(3, forumComment.getUserid());
            ps2.setString(4, forumComment.getCreatedAt());

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
                "JOIN msuser ON msuser.UserID = forum_comment.UserID\n" +
                "JOIN msforum ON msforum.ForumID = forum_comment.ForumID\n" +
                "WHERE forum_comment.ForumID = ?\n" +
                "ORDER BY forum_comment.CreatedAt DESC\n" +
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

}

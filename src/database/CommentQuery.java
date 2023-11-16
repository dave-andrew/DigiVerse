package database;

import model.ForumComment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}

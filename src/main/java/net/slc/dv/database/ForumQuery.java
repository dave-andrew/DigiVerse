package net.slc.dv.database;

import net.slc.dv.database.connection.Connect;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.Forum;
import net.slc.dv.model.User;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class ForumQuery {

    private final Connect connect;

    public ForumQuery() {
        this.connect = Connect.getConnection();
    }

    public ArrayList<Forum> getClassroomForum(String classid) {
        ArrayList<Forum> forumList = new ArrayList<>();
        String query = "SELECT\n" +
                "    class_forum.ForumID, ForumText, class_forum.UserID, UserName, UserEmail, UserDOB, UserProfile, class_forum.ClassID, ClassName, ClassDesc, ClassCode, ClassSubject, ClassImage, CreatedAt\n" +
                "FROM class_forum\n" +
                "JOIN msclass ON class_forum.ClassID = msclass.ClassID\n" +
                "JOIN msuser ON class_forum.UserID = msuser.UserID\n" +
                "JOIN msforum ON class_forum.ForumID = msforum.ForumID\n" +
                "WHERE class_forum.ClassID = ?\n" +
                "ORDER BY CreatedAt DESC";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, classid);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs);
                    Classroom classroom = new Classroom(rs);

                    forumList.add(new Forum(rs.getString("ForumID"), rs.getString("ForumText"), rs.getString("UserID"), user, rs.getString("ClassID"), classroom, rs.getString("CreatedAt")));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return forumList;
    }

    public Forum createForum(Forum forum) {
        String query = "INSERT INTO msforum VALUES (?, ?, ?)";
        String query2 = "INSERT INTO class_forum VALUES (?, ?, ?)";
        String query3 = "SELECT * FROM msuser WHERE UserID = ?";

        try (PreparedStatement ps = connect.prepareStatement(query);
             PreparedStatement ps2 = connect.prepareStatement(query2);
             PreparedStatement ps3 = connect.prepareStatement(query3)) {
            assert ps != null;
            ps.setString(1, forum.getId());
            ps.setString(2, forum.getText());
            ps.setString(3, forum.getCreatedAt());

            ps.executeUpdate();

            assert ps2 != null;
            ps2.setString(1, forum.getClassid());
            ps2.setString(2, forum.getId());
            ps2.setString(3, forum.getUserid());

            ps2.executeUpdate();

            assert ps3 != null;
            ps3.setString(1, forum.getUserid());

            try (var rs = ps3.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs);
                    forum.setUser(user);
                }
            }

            ToastBuilder.buildNormal().setText(TextStorage.getText(Text.FORUM_POSTED)).build();

            return forum;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

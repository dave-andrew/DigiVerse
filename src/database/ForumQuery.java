package database;

import database.connection.Connect;
import enums.ToastType;
import helper.toast.ToastBuilder;
import model.Classroom;
import model.Forum;
import model.User;

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
                    User user = new User(rs.getString("UserID"), rs.getString("UserName"), rs.getString("UserEmail"), "", rs.getString("UserDOB"), rs.getBlob("UserProfile"));
                    Classroom classroom = new Classroom(rs.getString("ClassID"), rs.getString("ClassName"), rs.getString("ClassDesc"), rs.getString("ClassCode"), rs.getString("ClassSubject"), rs.getBlob("ClassImage"));

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

        try (PreparedStatement ps = connect.prepareStatement(query);
             PreparedStatement ps2 = connect.prepareStatement(query2)) {
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

            ToastBuilder.buildNormal(ToastType.NORMAL).setText("Forum Posted!").build();

            return forum;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

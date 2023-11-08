package database;

import helper.StageManager;
import helper.Toast;
import model.Classroom;
import model.Forum;
import model.LoggedUser;
import model.User;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class ForumQuery {

    private Connect connect;
    private LoggedUser loggedUser;

    public ForumQuery() {
        loggedUser = LoggedUser.getInstance();
        this.connect = Connect.getConnection();
    }

    public ArrayList<Forum> getClassroomForum(String classid) {
        ArrayList<Forum> forumList = new ArrayList<>();
        String query = "SELECT " +
                "ForumID, ForumText, msforum.UserID, UserName, UserEmail, UserAge, UserProfile, msforum.ClassID, ClassName, ClassDesc, ClassCode, ClassSubject, ClassImage " +
                "FROM msforum WHERE ClassID = ?" +
                "JOIN msuser ON msforum.UserID = msuser.UserID" +
                "JOIN msclass ON msforum.ClassID = msclass.ClassID" +
                "ORDER BY ForumCreatedAt DESC";

        PreparedStatement ps = connect.prepareStatement(query);
        try {
            assert ps != null;
            ps.setString(1, classid);

            try(var rs = ps.executeQuery()) {
                while(rs.next()) {
                    User user = new User(rs.getString("UserID"), rs.getString("UserName"), rs.getString("UserEmail"), "", rs.getInt("UserAge"), rs.getBlob("UserProfile"));
                    Classroom classroom = new Classroom(rs.getString("ClassID"), rs.getString("ClassName"), rs.getString("ClassDesc"), rs.getString("ClassCode"), rs.getString("ClassSubject"), rs.getBlob("ClassImage"));

                    forumList.add(new Forum(rs.getString("ForumID"), rs.getString("ForumText"), rs.getString("UserID"), user, rs.getString("ClassID"), classroom, rs.getString("ForumCreatedAt")));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return forumList;
    }

    public Forum createForum(Forum forum) {
        String query = "INSERT INTO msforum VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = connect.prepareStatement(query);
        try {
            assert ps != null;
            ps.setString(1, forum.getId());
            ps.setString(2, forum.getText());
            ps.setString(3, forum.getUserid());
            ps.setString(4, forum.getClassid());
            ps.setString(5, forum.getCreatedAt());

            ps.executeUpdate();

            Toast.makeText(StageManager.getInstance(), "Forum created!", 2000, 500, 500);

            return forum;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

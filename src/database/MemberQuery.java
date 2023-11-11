package database;

import model.ClassroomMember;
import model.LoggedUser;
import model.User;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class MemberQuery {

    private Connect connect;

    public MemberQuery() {
        this.connect = Connect.getConnection();
    }
    public ArrayList<ClassroomMember> getClassMember(String classCode) {
        ArrayList<ClassroomMember> classroomMembers = new ArrayList<>();

        String query = "SELECT\n" +
                "\tClassID, Role, msuser.UserID, UserName, UserEmail, UserAge, UserProfile\n" +
                "FROM class_member\n" +
                "JOIN msuser\n" +
                "ON class_member.UserID = msuser.UserID\n" +
                "WHERE ClassID = ?";


        try(PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, classCode);

            try(var rs = ps.executeQuery()) {
                while(rs.next()) {
                    User user = new User(rs.getString("UserID"), rs.getString("UserName"), rs.getString("UserEmail"), "", rs.getInt("UserAge"), rs.getBlob("UserProfile"));
                    classroomMembers.add(new ClassroomMember(rs.getString("ClassID"), user, rs.getString("Role")));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return classroomMembers;
    }

    public String getRole(String classCode) {
        String role = "";

        String query = "SELECT Role FROM class_member WHERE UserID = ? AND ClassID = ?";

        try(PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, LoggedUser.getInstance().getId());
            ps.setString(2, classCode);

            try(var rs = ps.executeQuery()) {
                while(rs.next()) {
                    role = rs.getString("Role");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("User Role: " + role);
        return role;
    }

}

package database;

import model.ClassroomMember;
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

}

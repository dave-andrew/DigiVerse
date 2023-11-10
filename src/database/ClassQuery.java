package database;

import model.Classroom;
import model.ClassroomMember;
import model.LoggedUser;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ClassQuery {
    private LoggedUser loggedUser;

    private Connect connect;

    public ClassQuery() {
        loggedUser = LoggedUser.getInstance();
        this.connect = Connect.getConnection();
    }

    public void createClass(Classroom classroom) {

        String query = "INSERT INTO msclass VALUES (?, ?, ?, ?, ?, NULL)";
        String query2 = "INSERT INTO class_member VALUES (?, ?, ?)";

        PreparedStatement ps = connect.prepareStatement(query);
        PreparedStatement ps2 = connect.prepareStatement(query2);
        try {
            ps.setString(1, classroom.getClassId());
            ps.setString(2, classroom.getClassName());
            ps.setString(3, classroom.getClassDesc());
            ps.setString(4, classroom.getClassCode());
            ps.setString(5, classroom.getClassSubject());

            ps.executeUpdate();

            ps2.setString(1, classroom.getClassId());
            ps2.setString(2, loggedUser.getId());
            ps2.setString(3, "Teacher");

            ps2.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Classroom> getUserClassrooom() {
        ArrayList<Classroom> classrooms = new ArrayList<>();

        String query = "SELECT * FROM msclass WHERE ClassID IN (SELECT ClassID FROM class_member WHERE UserID = ?)";

        PreparedStatement ps = connect.prepareStatement(query);
        try {
            ps.setString(1, loggedUser.getId());

            try(var rs = ps.executeQuery()) {
                while(rs.next()) {
                    classrooms.add(new Classroom(rs.getString("ClassID"), rs.getString("ClassName"), rs.getString("ClassDesc"), rs.getString("ClassCode"), rs.getString("ClassSubject"), rs.getBlob("ClassImage")));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return classrooms;
    }

    public Classroom joinClass(String classCode) {
        String checkGroupClassCode = "SELECT * FROM msclass WHERE ClassCode = ?";
        String query = "INSERT INTO class_member VALUES (?, ?, ?)";

        PreparedStatement checkGroupCode = connect.prepareStatement(checkGroupClassCode);
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            checkGroupCode.setString(1, classCode);
            ResultSet rs = checkGroupCode.executeQuery();
            if(!rs.next()){
                return null;
            }

            ps.setString(1, rs.getString("ClassID"));
            ps.setString(2, loggedUser.getId());
            ps.setString(3, "Student");

            ps.executeUpdate();

            return new Classroom(rs.getString("ClassID"), rs.getString("ClassName"), rs.getString("ClassDesc"), rs.getString("ClassCode"), rs.getString("ClassSubject"), rs.getBlob("ClassImage"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

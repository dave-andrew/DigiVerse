package database;

import model.Classroom;
import model.LoggedUser;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class ClassQuery {
    private LoggedUser loggedUser;

    private Connect connect;

    public ClassQuery() {
        loggedUser = LoggedUser.getInstance();
        this.connect = Connect.getConnection();
    }

    public void createClass(Classroom classroom) {

        String query = "INSERT INTO msclass VALUES (?, ?, ?, ?, ?)";
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
                    classrooms.add(new Classroom(rs.getString("ClassID"), rs.getString("ClassName"), rs.getString("ClassDesc"), rs.getString("ClassCode"), rs.getString("ClassSubject")));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return classrooms;
    }

    public void joinClass(String classCode) {
        String query = "INSERT INTO class_member VALUES (?, ?, ?)";

        PreparedStatement ps = connect.prepareStatement(query);
        try {
            ps.setString(1, classCode);
            ps.setString(2, loggedUser.getId());
            ps.setString(3, "Student");

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

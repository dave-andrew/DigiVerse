package net.slc.dv.database;

import net.slc.dv.database.connection.Connect;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.LoggedUser;

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
            assert ps != null;
            ps.setString(1, classroom.getClassId());
            ps.setString(2, classroom.getClassName());
            ps.setString(3, classroom.getClassDesc());
            ps.setString(4, classroom.getClassCode());
            ps.setString(5, classroom.getClassSubject());

            ps.executeUpdate();

            assert ps2 != null;
            ps2.setString(1, classroom.getClassId());
            ps2.setString(2, loggedUser.getId());
            ps2.setString(3, "Teacher");

            ps2.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Classroom> getUserClassroom() {
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
//            throw new RuntimeException(e);
        }

        return classrooms;
    }

    public String joinClass(String classCode) {
        String checkGroupClassCode = "SELECT * FROM msclass WHERE ClassCode = ?";
        String query = "INSERT INTO class_member VALUES (?, ?, ?)";

        PreparedStatement checkGroupCode = connect.prepareStatement(checkGroupClassCode);
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            assert checkGroupCode != null;
            checkGroupCode.setString(1, classCode);
            ResultSet rs = checkGroupCode.executeQuery();
            if(!rs.next()){
                return "no data";
            }

            assert ps != null;
            ps.setString(1, rs.getString("ClassID"));
            ps.setString(2, loggedUser.getId());
            ps.setString(3, "Student");

            ps.executeUpdate();

            return rs.getString("ClassID");
        } catch (Exception e) {
            return "ingroup";
//            throw new RuntimeException(e);
        }
    }
}

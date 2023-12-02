package net.slc.dv.database;

import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.helper.Closer;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.LoggedUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassQuery {

    private final LoggedUser loggedUser;

    public ClassQuery() {
        this.loggedUser = LoggedUser.getInstance();
    }

    public void createClass(Classroom classroom) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("msclass")
                    .values("ClassID", classroom.getClassId())
                    .values("ClassName", classroom.getClassName())
                    .values("ClassDesc", classroom.getClassDesc())
                    .values("ClassCode", classroom.getClassCode())
                    .values("ClassSubject", classroom.getClassSubject());

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("class_member")
                    .values("ClassID", classroom.getClassId())
                    .values("UserID", loggedUser.getId())
                    .values("Role", "Teacher");

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Classroom> getUserClassroom() {
        ArrayList<Classroom> classrooms = new ArrayList<>();

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("msclass")
                    .join("msclass", "ClassID", "class_member", "ClassID")
                    .condition("UserID", "=", loggedUser.getId());

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            while (set.next()) {
                classrooms.add(new Classroom(set));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classrooms;
    }

    public String joinClass(String classCode) {
        String classId;
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("msclass")
                    .condition("ClassCode", "=", classCode);

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            if (!set.next()) {
                return "no data";
            }

            classId = set.getString("ClassID");
        } catch (SQLException e) {
            return "ingroup";
        }

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("class_member")
                    .values("ClassID", classId)
                    .values("UserID", loggedUser.getId())
                    .values("Role", "Student");

            closer.add(queryBuilder.getResults());
            return classId;
        } catch (SQLException e) {
            return "ingroup";
        }
    }
}

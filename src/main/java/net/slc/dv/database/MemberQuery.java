package net.slc.dv.database;

import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.helper.Closer;
import net.slc.dv.model.ClassroomMember;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MemberQuery {

    public ArrayList<ClassroomMember> getClassMember(String classCode) {
        ArrayList<ClassroomMember> classroomMembers = new ArrayList<>();

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("class_member")
                    .columns("ClassID", "Role", "msuser.UserID", "UserName", "UserEmail", "UserDOB", "UserProfile")
                    .join("class_member", "UserID", "msuser", "UserID")
                    .condition("ClassID", "=", classCode);

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            while (set.next()) {
                User user = new User(set);

                ClassroomMember classroomMember = new ClassroomMember(set.getString("ClassID"), user, set.getString("Role"));
                classroomMembers.add(classroomMember);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classroomMembers;
    }

    public String getRole(String classCode) {
        String role = "";

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("class_member")
                    .columns("Role")
                    .condition("UserID", "=", LoggedUser.getInstance().getId())
                    .condition("ClassID", "=", classCode);

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            while (set.next()) {
                role = set.getString("Role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }

}

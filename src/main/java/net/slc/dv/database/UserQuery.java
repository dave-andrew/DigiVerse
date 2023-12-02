package net.slc.dv.database;

import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.helper.Closer;
import net.slc.dv.model.LoggedUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserQuery {

    public void updateProfileImage(File file) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.UPDATE)
                    .table("msuser")
                    .values("UserProfile", new FileInputStream(file))
                    .condition("UserID", "=", LoggedUser.getInstance().getId());

            closer.add(queryBuilder.getResults());
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String updateProfile(String name, String email, String birthday) {
        String query = "UPDATE msuser SET UserName = ?, UserEmail = ?, UserDOB = ? WHERE UserID = ?";

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.UPDATE)
                    .table("msuser")
                    .values("UserName", name)
                    .values("UserEmail", email)
                    .values("UserDOB", birthday)
                    .condition("UserID", "=", LoggedUser.getInstance().getId());

            closer.add(queryBuilder.getResults());
            return "Success";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Unexpected Error";
        }
    }

    public boolean validateOldPassword(String oldPassword) {
        String query = "SELECT COUNT(*) FROM msuser WHERE UserPassword = ? AND UserID = ?";
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("msuser")
                    .columns("COUNT(*)")
                    .condition("UserPassword", "=", oldPassword)
                    .condition("UserID", "=", LoggedUser.getInstance().getId());

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            if (set.next()) {
                return set.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String updatePassword(String newPassword) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.UPDATE)
                    .table("msuser")
                    .values("UserPassword", newPassword)
                    .condition("UserID", "=", LoggedUser.getInstance().getId());

            closer.add(queryBuilder.getResults());
            return "Success";
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Unexpected Error";
    }

}

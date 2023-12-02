package net.slc.dv.database;

import net.slc.dv.database.builder.NeoQueryBuilder;
import net.slc.dv.database.builder.Results;
import net.slc.dv.database.builder.enums.QueryType;
import net.slc.dv.helper.Closer;
import net.slc.dv.helper.DateManager;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.model.User;

import java.net.InetAddress;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AuthQuery {

    public void register(User user) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("msuser")
                    .values("UserID", user.getId())
                    .values("UserName", user.getUsername())
                    .values("UserEmail", user.getEmail())
                    .values("UserPassword", user.getPassword())
                    .values("UserDOB", user.getDob());

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User login(String email, String pass) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("msuser")
                    .condition("UserEmail", "=", email)
                    .condition("UserPassword", "=", pass)
                    .limit(1);

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            if (!set.next()) {
                return null;
            }

            String userID = set.getString("UserID");
            String userName = set.getString("UserName");
            String userEmail = set.getString("UserEmail");
            String userPassword = set.getString("UserPassword");
            String userDOB = set.getString("UserDOB");
            Blob userProfile = set.getBlob("UserProfile");

            return new User(userID, userName, userEmail, userPassword, userDOB, userProfile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rememberMe(User user) {
        String computerName;
        try {
            computerName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            computerName = System.getenv("COMPUTERNAME");
        }

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.INSERT)
                    .table("authcheck")
                    .values("DeviceName", computerName)
                    .values("UserID", user.getId())
                    .values("expired", DateManager.formatDate(LocalDateTime.now().plusDays(1)));

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String checkAuth() {
        LocalDateTime now = LocalDateTime.now();
        String computerName;
        try {
            computerName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            computerName = System.getenv("COMPUTERNAME");
        }

        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.SELECT)
                    .table("authcheck")
                    .join("authcheck", "UserID", "msuser", "UserID")
                    .condition("DeviceName", "=", computerName);

            Results results = closer.add(queryBuilder.getResults());
            ResultSet set = closer.add(results.getResultSet());
            if (set.next()) {
                String expired = set.getString("expired");
                LocalDateTime expiredDate = DateManager.parseDate(expired);
                if (now.isAfter(expiredDate)) {
                    deleteAuthData(computerName);
                    return "false";
                }

                String userID = set.getString("UserID");
                String userName = set.getString("UserName");
                String userEmail = set.getString("UserEmail");
                String userPassword = set.getString("UserPassword");
                String userDOB = set.getString("UserDOB");
                Blob userProfile = set.getBlob("UserProfile");
                User user = new User(userID, userName, userEmail, userPassword, userDOB, userProfile);
                LoggedUser.initialize(user);
                return "true";
            }

            return "false";
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public void deleteAuthData(String deviceName) {
        try (Closer closer = new Closer()) {
            NeoQueryBuilder queryBuilder = new NeoQueryBuilder(QueryType.DELETE)
                    .table("authcheck")
                    .condition("DeviceName", "=", deviceName);

            closer.add(queryBuilder.getResults());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

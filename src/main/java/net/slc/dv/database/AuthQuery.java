package net.slc.dv.database;

import net.slc.dv.database.builder.QueryBuilder;
import net.slc.dv.database.connection.Connect;
import net.slc.dv.helper.Closer;
import net.slc.dv.helper.DateManager;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.model.User;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AuthQuery {

    private final Connect connect;

    public AuthQuery() {
        this.connect = Connect.getConnection();
    }

    public void register(User user) {
        try (Closer closer = new Closer()) {
            PreparedStatement ps = closer.add(new QueryBuilder().into("msuser").values("(?, ?, ?, ?, ?, NULL)").insert());
            ps.setString(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getDob());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User login(String email, String pass) {
        String query = "SELECT * FROM msuser WHERE UserEmail = ? AND UserPassword = ? LIMIT 1";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, email);
            ps.setString(2, pass);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("UserID"), rs.getString("UserName"), rs.getString("UserEmail"), rs.getString("UserPassword"), rs.getString("UserDOB"), rs.getBlob("UserProfile"));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean rememberMe(User user) {
        String query = "INSERT INTO authcheck VALUES (?, ?, ?)";

        String computerName;
        try {
            computerName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            computerName = System.getenv("COMPUTERNAME");
        }

        LocalDateTime now = LocalDateTime.now();
        now = now.plusDays(1);
        String formattedDate = DateManager.formatDate(now);

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, computerName);
            ps.setString(2, user.getId());
            ps.setString(3, formattedDate);

            int rows = ps.executeUpdate();

            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String checkAuth() {
        LocalDateTime now = LocalDateTime.now();

        String query = "SELECT * FROM authcheck AS a JOIN msuser AS u ON a.UserID = u.UserID WHERE a.DeviceName = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            String computerName;
            try {
                computerName = InetAddress.getLocalHost().getHostName();
            } catch (Exception e) {
                computerName = System.getenv("COMPUTERNAME");
            }

            if (ps != null) {
                ps.setString(1, computerName);
            } else {
                return "error";
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDateTime expiredDate = DateManager.parseDate(rs.getString("expired"));
                    if (now.isAfter(expiredDate)) {
                        deleteAuthData(computerName);
                        return "false";
                    } else {
                        User user = new User(rs.getString("UserID"), rs.getString("UserName"), rs.getString("UserEmail"), rs.getString("UserPassword"), rs.getString("UserDOB"), rs.getBlob("UserProfile"));
                        LoggedUser.initialize(user);
                        return "true";
                    }
                } else {
                    return "false";
                }
            }
        } catch (SQLException e) {
            return "error";
        }
    }

    public void deleteAuthData(String deviceName) {
        String deleteQuery = "DELETE FROM authcheck WHERE DeviceName = ?";

        try (PreparedStatement deleteStatement = connect.prepareStatement(deleteQuery)) {
            assert deleteStatement != null;
            deleteStatement.setString(1, deviceName);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting expired data: " + e.getMessage());
        }
    }

}
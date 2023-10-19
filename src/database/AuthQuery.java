package database;

import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuthQuery {

    private Connect connect;

    public AuthQuery() {
        this.connect = Connect.getConnection();
    }

    public boolean register(User user) {
        String query = "INSERT INTO msuser VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = connect.prepareStatement(query);
        try {
            ps.setString(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getAge());

            int rows = ps.executeUpdate();

            if(rows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User login(String email, String pass) {
        String query = "SELECT * FROM msuser WHERE UserEmail = ? AND UserPassword = ?";

        PreparedStatement ps = connect.prepareStatement(query);
        try {
            ps.setString(1, email);
            ps.setString(2, pass);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return new User(rs.getString("UserID"), rs.getString("UserName"), rs.getString("UserEmail"), rs.getString("UserPassword"), rs.getInt("UserAge"));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean rememberMe(User user) {
        String query = "INSERT INTO authcheck VALUES (?, ?)";

        LocalDateTime now = LocalDateTime.now();
        now = now.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);

        PreparedStatement ps = connect.prepareStatement(query);
        try {
            ps.setString(1, user.getId());
            ps.setString(2, formattedDate);

            int rows = ps.executeUpdate();

            if(rows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

package database;

import model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}

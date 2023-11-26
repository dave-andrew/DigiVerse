package database;

import model.LoggedUser;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserQuery {

    private final Connect connect;

    public UserQuery() {
        this.connect = Connect.getConnection();
    }

    public void updateProfileImage(File file) {
        String query = "UPDATE msuser SET UserProfile = ? WHERE UserID = ?";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setBlob(1, new FileInputStream(file));
            ps.setString(2, LoggedUser.getInstance().getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String updateProfile(String name, String email, String birthday) {
        String query = "UPDATE msuser SET UserName = ?, UserEmail = ?, UserDOB = ? WHERE UserID = ?";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, birthday);
            ps.setString(4, LoggedUser.getInstance().getId());

            ps.executeUpdate();
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Unexpected Error";
        }

    }

    public boolean validateOldPassword(String oldPassword) {
        String query = "SELECT COUNT(*) FROM msuser WHERE UserPassword = ? AND UserID = ?";
        try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {

            assert preparedStatement != null;
            preparedStatement.setString(1, oldPassword);
            preparedStatement.setString(2, LoggedUser.getInstance().getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public String updatePassword(String newPassword) {
        String updateQuery = "UPDATE msuser SET UserPassword = ? WHERE UserID = ?";
        try (PreparedStatement preparedStatement = connect.prepareStatement(updateQuery)) {

            assert preparedStatement != null;
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, LoggedUser.getInstance().getId());
            preparedStatement.executeUpdate();
            return "Success";
        } catch (SQLException e) {
            return "Unexpected Error";
        }
    }

}

package database;

import helper.ImageManager;
import model.LoggedUser;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserQuery {

    private Connect con;

    public UserQuery() {
        this.con = Connect.getConnection();
    }

    public void updateProfileImage(File file) {
        String query = "UPDATE msuser SET UserProfile = ? WHERE UserID = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            assert ps != null;
            ps.setBlob(1, new FileInputStream(file));
            ps.setString(2, LoggedUser.getInstance().getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean updateProfile(String name, String email, String birthday) {
        String query = "UPDATE msuser SET UserName = ?, UserEmail = ?, UserDOB = ? WHERE UserID = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, birthday);
            ps.setString(4, LoggedUser.getInstance().getId());

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean validateOldPassword(String oldPassword) {
        String query = "SELECT COUNT(*) FROM msuser WHERE UserPassword = ? AND UserID = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {

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
//            throw new RuntimeException(e);
        }
    }

    public boolean updatePassword(String newPassword) {
        String updateQuery = "UPDATE msuser SET UserPassword = ? WHERE UserID = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {

            assert preparedStatement != null;
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, LoggedUser.getInstance().getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
//            throw new RuntimeException(e);
        }
    }

}

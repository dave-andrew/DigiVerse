package database;

import helper.ImageManager;
import model.LoggedUser;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;

public class UserQuery {

    private Connect con;

    public UserQuery() {
        this.con = Connect.getConnection();
    }

    public void updateProfileImage(File file) {
        String query = "UPDATE msuser SET UserProfile = ? WHERE UserID = ?";

        try(PreparedStatement ps = con.prepareStatement(query)) {
            assert ps != null;
            ps.setBlob(1, new FileInputStream(file));
            ps.setString(2, LoggedUser.getInstance().getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean updateProfile(String name, String email, int birthday) {
        String query = "UPDATE msuser SET UserName = ?, UserEmail = ?, UserAge = ? WHERE UserID = ?";

        try(PreparedStatement ps = con.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, birthday);
            ps.setString(4, LoggedUser.getInstance().getId());

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}

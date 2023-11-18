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

}

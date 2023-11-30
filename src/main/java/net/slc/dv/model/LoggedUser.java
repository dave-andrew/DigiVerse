package net.slc.dv.model;

import net.slc.dv.helper.ImageManager;
import javafx.scene.image.Image;

import java.sql.Blob;

public class LoggedUser extends User {

    private static LoggedUser loggedUser;
    private Image profileImage;

    private LoggedUser(String id, String username, String email, String password, String dob, Blob profile) {
        super(id, username, email, password, dob, profile);

        if (profile != null) {
            this.profileImage = ImageManager.convertBlobImage(profile);
        }
    }

    public static void initialize(User user) {
        if (loggedUser != null) {
            return;
        }

        loggedUser = new LoggedUser(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getDob(), user.getBlobProfile());
    }

    public static LoggedUser getInstance() {
        assert loggedUser != null;

        return loggedUser;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image image) {
        this.profileImage = image;
    }

    public void logout() {
        loggedUser = null;
    }
}

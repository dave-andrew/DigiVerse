package model;

import helper.ImageManager;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.sql.Blob;

public class LoggedUser extends User{

    private static LoggedUser loggedUser;
    private Image profileImage;

    public static LoggedUser getInstance(User user) {
        if(loggedUser == null) {
            loggedUser = new LoggedUser(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getAge(), user.getBlobProfile());
        }
        return loggedUser;
    }

    public static LoggedUser getInstance() {
        return loggedUser;
    }

    private LoggedUser(String id, String username, String email, String password, int age, Blob profile) {
        super(id, username, email, password, age, profile);

        if(profile != null){
            this.profileImage = ImageManager.convertBlobImage(profile);
        }
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

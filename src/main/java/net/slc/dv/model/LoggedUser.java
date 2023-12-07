package net.slc.dv.model;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import net.slc.dv.helper.ImageManager;

import java.sql.Blob;

@Getter
@Setter
public class LoggedUser extends User {

    private static LoggedUser loggedUser;
    private Image profileImage;

    private LoggedUser(String id, String username, String email, String password, String dob, Blob profile, String color) {
        super(id, username, email, password, dob, profile, color);

        if (profile != null) {
            this.profileImage = ImageManager.convertBlobImage(profile);
        }
    }

    public static void initialize(User user) {
        if (loggedUser != null) {
            return;
        }

        loggedUser = new LoggedUser(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getDob(), user.getBlobProfile(), user.getColor());
    }

    public static LoggedUser getInstance() {
        assert loggedUser != null;

        return loggedUser;
    }

    public void logout() {
        loggedUser = null;
    }

}

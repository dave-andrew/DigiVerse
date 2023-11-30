package net.slc.dv.model;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class User {

    private final String id;
    private String username;
    private String email;
    private String password;
    private String dob;
    private Image profile;
    private Blob blobProfile;

    public User(String id, String username, String email, String password, String dob, Blob profile) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dob = dob;

        try {
            if (profile != null) {
                this.blobProfile = profile;
                InputStream in = profile.getBinaryStream();
                this.profile = new Image(in);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User(String username, String email, String password, String dob) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
        this.dob = dob;
    }

    public User(String id, String username, String email, String password, String dob) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dob = dob;
    }

}

package net.slc.dv.model;

import javafx.scene.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class User {

    private String id;
    private String username;
    private String email;
    private String password;
    private String dob;
    private Image profile;
    private Blob blobProfile;
    private String color;

    public User(String id, String username, String email, String password, String dob, Blob profile, String color) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.color = color;

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

    public User(ResultSet resultSet) {
        try {
            this.id = resultSet.getString("UserID");
            this.username = resultSet.getString("Username");
            this.email = resultSet.getString("UserEmail");
            this.dob = resultSet.getString("UserDOB");

            Blob profile = resultSet.getBlob("UserProfile");
            if (profile != null) {
                this.blobProfile = profile;
                InputStream in = profile.getBinaryStream();
                this.profile = new Image(in);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            this.password = resultSet.getString("UserPassword");
        } catch (SQLException e) {
            this.password = null;
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

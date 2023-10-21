package model;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.UUID;

public class User {

    private final String id;
    private String username;
    private String email;
    private String password;
    private int age;
    private Image profile;
    private Blob blobProfile;

    public User(String id, String username, String email, String password, int age, Blob profile) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;

        try {
            if(profile != null) {
                this.blobProfile = profile;
                InputStream in = profile.getBinaryStream();
                this.profile = new Image(in);
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User(String username, String email, String password, int age) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public User(String id, String username, String email, String password, int age) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public Image getProfile() {
        return profile;
    }

    public void setProfile(Image profile) {
        this.profile = profile;
    }

    public Blob getBlobProfile() {
        return blobProfile;
    }

    public void setBlobProfile(Blob blobProfile) {
        this.blobProfile = blobProfile;
    }
}

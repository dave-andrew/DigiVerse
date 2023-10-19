package model;

import java.util.UUID;

public class User {

    private final String id;
    private String username;
    private String email;
    private String password;
    private int age;

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
}

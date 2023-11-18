package controller;

import database.UserQuery;

import java.io.File;

public class UserController {

    private UserQuery userQuery;

    public UserController() {
        this.userQuery = new UserQuery();
    }

    public void updateProfileImage(File file) {
        userQuery.updateProfileImage(file);
    }

    public boolean updateProfile(String name, String email, int birthday) {

        if(name.isEmpty() || email.isEmpty() || birthday == 0) {
            return false;
        }

        return userQuery.updateProfile(name, email, birthday);
    }

}

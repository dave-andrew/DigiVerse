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

}

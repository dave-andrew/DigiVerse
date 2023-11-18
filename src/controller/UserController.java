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

    public boolean updatePassword(String oldPassword, String newPassword, String confirmPassword) {
        if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            return false;
        } else if(!newPassword.equals(confirmPassword)) {
            return false;
        } else if(!userQuery.validateOldPassword(oldPassword)) {
            return false;
        }

        return userQuery.updatePassword(newPassword);
    }

}

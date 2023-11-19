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

    public String updateProfile(String name, String email, String birthday) {

        if(name.isEmpty() || email.isEmpty() || birthday.isEmpty()) {
            return "All fields must be filled!";
        }

        return userQuery.updateProfile(name, email, birthday);
    }

    public String updatePassword(String oldPassword, String newPassword, String confirmPassword) {
        if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            return "All fields must be filled!";
        } else if(!newPassword.equals(confirmPassword)) {
            return "New password and confirm password must be same!";
        } else if(!userQuery.validateOldPassword(oldPassword)) {
            return "Old password is wrong!";
        }

        return userQuery.updatePassword(newPassword);
    }

}

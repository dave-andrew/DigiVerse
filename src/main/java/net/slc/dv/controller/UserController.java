package net.slc.dv.controller;

import net.slc.dv.database.UserQuery;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;

import java.io.File;

public class UserController {

    private final UserQuery userQuery;

    public UserController() {
        this.userQuery = new UserQuery();
    }

    public void updateProfileImage(File file) {
        userQuery.updateProfileImage(file);
    }

    public String updateProfile(String name, String email, String birthday) {

        if(name.isEmpty() || email.isEmpty() || birthday.isEmpty()) {
            return TextStorage.getText(Text.ALL_FIELDS_MUST_BE_FILLED);
        }

        return userQuery.updateProfile(name, email, birthday);
    }

    public String updatePassword(String oldPassword, String newPassword, String confirmPassword) {
        if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            return TextStorage.getText(Text.ALL_FIELDS_MUST_BE_FILLED);
        } else if(!newPassword.equals(confirmPassword)) {
            return TextStorage.getText(Text.NEW_PASSWORD_AND_CONFIRM_PASSWORD_MUST_BE_SAME);
        } else if(!userQuery.validateOldPassword(oldPassword)) {
            return TextStorage.getText(Text.OLD_PASSWORD_IS_WRONG);
        }

        return userQuery.updatePassword(newPassword);
    }

    public void updateColor(String color) {
        userQuery.updateColor(color);
    }

}

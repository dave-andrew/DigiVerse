package net.slc.dv.controller;

import net.slc.dv.database.AuthQuery;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.model.User;

import java.net.InetAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AuthController {

    private final AuthQuery authQuery;

    public AuthController() {
        this.authQuery = new AuthQuery();
    }

    public String checkRegister(String username, String email, String pass, String confirmPass, String dob) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            return "Please fill all the fields!";
        }

        if (!pass.equals(confirmPass)) {
            return "Password and confirm password must be the same!";
        }

        if (pass.length() < 8) {
            return "Password must be greater than 8 characters!";
        }

        if (!email.contains("@") && !email.contains(".")) {
            return "Invalid email format!";
        }

        LocalDate dateOfBirth = LocalDate.parse(dob, formatter);

        int age = LocalDate.now().getYear() - dateOfBirth.getYear();

        if (age < 17) {
            return "Age must be greater than 17!";
        }

        User user = new User(username, email, pass, dob);
        authQuery.register(user);
        return "Register Success!";
    }

    public String checkLogin(String email, String pass, boolean remember) {
        if (email.isEmpty() || pass.isEmpty()) {
            return "Please fill all the fields!";
        }

        User user = authQuery.login(email, pass);


        if (user == null) {
            return "Email or password is incorrect!";
        }

        if (remember) {
            authQuery.rememberMe(user);
        }

        LoggedUser.initialize(user);
        return "Login Success!";
    }

    public String checkAuth() {
        return authQuery.checkAuth();
    }

    public void removeAuth() {
        String computerName;
        try {
            computerName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            computerName = System.getenv("COMPUTERNAME");
        }

        authQuery.deleteAuthData(computerName);
    }
}

package controller;

import database.AuthQuery;
import model.User;

public class AuthController {

    private AuthQuery authQuery;

    public AuthController() {
        this.authQuery = new AuthQuery();
    }

    public String checkRegister(String username, String email, String pass, String confirmPass, int age) {
        if(username.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            return "Please fill all the fields!";
        } else if(!pass.equals(confirmPass)) {
            return "Password and confirm password must be same!";
        } else if(pass.length() < 8) {
            return "Password must be greater than 8 characters!";
        }else if(age < 17) {
            return "Age must be greater than 17!";
        } else {
            User user = new User(username, email, pass, age);
            authQuery.register(user);
            return "Register Success!";
        }
    }

    public String checkLogin(String email, String pass, boolean remember) {
        if(email.isEmpty() || pass.isEmpty()) {
            return "Please fill all the fields!";
        } else {
            User user = authQuery.login(email, pass);

            if(remember) {
                authQuery.rememberMe(user);
            }
            if(user == null) {
                return "Email or password is incorrect!";
            } else {
                return "Login Success!";
            }
        }
    }

    public boolean checkAuth() {
        return authQuery.checkAuth();
    }

}

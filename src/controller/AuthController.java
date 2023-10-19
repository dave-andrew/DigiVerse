package controller;

import model.User;

public class AuthController {



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

            return "Register Success!";
        }
    }

}

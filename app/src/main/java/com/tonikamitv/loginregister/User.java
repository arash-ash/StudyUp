package com.tonikamitv.loginregister;

public class User {

    String name, username, password, department;


    public User(String name, String department, String username, String password) {
        this.name = name;
        this.department = department;
        this.username = username;
        this.password = password;
    }

    // 2nd constructor for when the user does not provide department and name in the login activity

    public User(String username, String password)
    {
        this.username = username;
        this.password= password;
        this.department = "";
        this.name = "";
    }
}

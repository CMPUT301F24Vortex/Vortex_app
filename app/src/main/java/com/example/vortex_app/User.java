package com.example.vortex_app;

import java.util.List;

public class User {

    private final String userID;
    private String email;
    private String name;
    private String phoneNumber;
    private List<String> roles;


    // User class constructor
    // UserID is the only required
    User (String newUserID){
        this.userID = newUserID;
    }

    // User class getters
    public String getUserID () {
        return this.userID;
    }
    public String getEmail () {
        return this.email;
    }
    public String getName () {
        return this.name;
    }
    public String getPhoneNumber () {
        return this.phoneNumber;
    }
    public List<String> getRoles () {
        return this.roles;
    }

    // User class setters
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

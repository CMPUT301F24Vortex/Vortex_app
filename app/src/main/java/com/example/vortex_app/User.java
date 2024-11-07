package com.example.vortex_app;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String profilePicUrl;

    public User(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getName() {return firstName + " " + lastName;}
    public String getProfilePicUrl() {return profilePicUrl;}

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}

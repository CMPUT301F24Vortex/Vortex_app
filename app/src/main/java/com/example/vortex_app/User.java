package com.example.vortex_app;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String contactInfo;
    private String userID;

    // Required empty constructor for Firestore
    public User() {}

    public User(String firstName, String lastName, String email, String contactInfo, String userID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactInfo = contactInfo;
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getUserID() {
        return userID;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

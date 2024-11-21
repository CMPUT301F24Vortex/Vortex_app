package com.example.vortex_app;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String profilePicUrl;
    private String userID;

    // Constructor
    public User(String firstName, String lastName, String email, String phoneNumber, String userID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userID = userID; // Initialize userID during object creation
    }

    // Additional Constructor for backward compatibility (without userID)
    public User(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userID = null; // Set to null if not provided
    }

    // Getter for full name
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Getter for phone number
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Getter for name (alternative)
    public String getName() {
        return firstName + " " + lastName;
    }

    // Getter for profile picture URL
    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    // Getter for userID
    public String getUserID() {
        return userID;
    }

    // Setter for profile picture URL
    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    // Setter for userID
    public void setUserID(String userID) {
        this.userID = userID;
    }
}

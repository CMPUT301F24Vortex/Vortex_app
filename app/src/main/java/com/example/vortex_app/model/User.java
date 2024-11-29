package com.example.vortex_app.model;

/**
 * User class to represent a user profile.
 * This class is used for storing and retrieving user data from Firestore.
 */
public class User {
    private String firstName;       // User's first name
    private String lastName;        // User's last name
    private String email;           // User's email address
    private String contactInfo;     // User's contact information (matches Firestore)
    private String avatarUrl;       // URL of the user's profile picture
    private String userID;          // Unique ID of the user
    private String device;
    private String eventID;
    private Boolean notificationsEnabled;



    // No-argument constructor (required for Firestore serialization)
    public User() {}

    // Constructor to initialize mandatory fields
    public User(String firstName, String lastName, String email, String contactInfo, String device) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactInfo = contactInfo;
        this.device = device;
        //this.notificationsEnabled = notificationsEnabled;
    }

    public User(String firstName, String lastName, String userID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        //this.notificationsEnabled = true;
    }







    public String getFirstName() {
        return firstName;
    }



    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactInfo() { // Matches Firestore field name
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) { // Matches Firestore field name
        this.contactInfo = contactInfo;
    }

    public String getAvatarUrl() { // Matches Firestore field name
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) { // Matches Firestore field name
        this.avatarUrl = avatarUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDevice() { // Matches Firestore field name
        return device;
    }

    public void setDevice(String device) { // Matches Firestore field name
        this.device = device;
    }

    // Utility methods
    public String getFullName() {
        return firstName + " " + lastName;
    }


    public Boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", userID='" + userID + '\'' +
                ", device='" + device + '\'' +
                ", notificationsEnabled=" + notificationsEnabled +
                '}';
    }
}




package com.example.vortex_app.model;

/**
 * User class to represent a user profile or entrant.
 * This class is used for storing and retrieving user data from Firestore.
 */
public class User {
    private String userName; // User's username
    private String firstName; // User's first name
    private String lastName; // User's last name
    private String email; // User's email
    private String contactInfo; // User's contact information
    private String avatarUrl; // URL for user's profile picture
    private String userID; // User's unique ID
    private String device; // User's device information
    private String eventID; // Event ID associated with the user
    private String status; // Status of the user (e.g., confirmed, pending)
    private boolean isConfirmed; // Whether the user is confirmed

    // Default constructor
    public User() {}

    // Constructor for initializing mandatory fields
    public User(String firstName, String lastName, String email, String contactInfo, String device) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactInfo = contactInfo;
        this.device = device;
    }

    // Constructor with userName and other fields
    public User(String userName, String firstName, String lastName, String email, String contactInfo, String device) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactInfo = contactInfo;
        this.device = device;
    }

    // Constructor for event-based details
    public User(String userName, String firstName, String lastName, String userID, String eventID, String status, boolean isConfirmed) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.eventID = eventID;
        this.status = status;
        this.isConfirmed = isConfirmed;
    }

    // Constructor for waiting list users
    public User(String firstName, String lastName, String userID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
    }

    // Constructor for initializing userName and userID
    public User(String userName, String userID) {
        this.userName = userName;
        this.userID = userID;
    }

    // Getter and Setter methods
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Utility method for getting full name
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", userID='" + userID + '\'' +
                ", device='" + device + '\'' +
                ", eventID='" + eventID + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

package com.example.vortex_app;

/**
 * The User class represents a user with attributes such as first name, last name, email,
 * phone number, profile picture URL, and user ID. It provides methods to access and
 * modify these attributes.
 */
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String profilePicUrl;
    private String userID;

    /**
     * Constructs a new User with the given first name, last name, email, and phone number.
     *
     * @param firstName   The first name of the user.
     * @param lastName    The last name of the user.
     * @param email       The email of the user.
     * @param phoneNumber The phone number of the user.
     */
    public User(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the full name of the user (first name and last name concatenated).
     *
     * @return The full name of the user.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Returns the email of the user.
     *
     * @return The user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the phone number of the user.
     *
     * @return The user's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the name of the user (first name and last name concatenated).
     * This method is similar to getFullName().
     *
     * @return The name of the user.
     */
    public String getName() {
        return firstName + " " + lastName;
    }

    /**
     * Returns the profile picture URL of the user.
     *
     * @return The user's profile picture URL.
     */
    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    /**
     * Returns the user ID of the user.
     *
     * @return The user's user ID.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the profile picture URL of the user.
     *
     * @param profilePicUrl The new profile picture URL.
     */
    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    /**
     * Sets the user ID of the user.
     *
     * @param userID The new user ID.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }
}

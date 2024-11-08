package com.example.vortex_app;

/**
 * {@code User} is a model class that represents a user's profile data. It includes fields for
 * personal information such as name, email, phone number, profile picture URL, and a unique user ID.
 */
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String profilePicUrl;
    private String userID;

    /**
     * Constructs a {@code User} instance with the specified first name, last name, email, and phone number.
     *
     * @param firstName   The first name of the user.
     * @param lastName    The last name of the user.
     * @param email       The user's email address.
     * @param phoneNumber The user's phone number.
     */
    public User(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the full name of the user, combining the first and last names.
     *
     * @return A {@link String} representing the user's full name.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Returns the email address of the user.
     *
     * @return A {@link String} representing the user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the phone number of the user.
     *
     * @return A {@link String} representing the user's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the full name of the user.
     *
     * @return A {@link String} with the user's full name.
     */
    public String getName() {
        return firstName + " " + lastName;
    }

    /**
     * Returns the URL of the user's profile picture.
     *
     * @return A {@link String} representing the URL of the user's profile picture.
     */
    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    /**
     * Returns the unique ID of the user.
     *
     * @return A {@link String} representing the user's unique ID.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the URL for the user's profile picture.
     *
     * @param profilePicUrl A {@link String} representing the new URL for the profile picture.
     */
    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    /**
     * Sets the unique ID of the user.
     *
     * @param userID A {@link String} representing the new unique ID for the user.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }
}

package com.example.vortex_app.model;

/**
 * User class to represent a user profile or entrant.
 * This class is used for storing and retrieving user data from Firestore.
 */
public class User {

    /**
     * The user's first name.
     */
    private String firstName;

    /**
     * The user's last name.
     */
    private String lastName;

    /**
     * The user's email address.
     */
    private String email;

    /**
     * The user's contact information (e.g., phone number).
     */
    private String contactInfo;

    /**
     * The URL for the user's profile picture (avatar).
     */
    private String avatarUrl;

    /**
     * The unique identifier for the user.
     */
    private String userID;

    /**
     * The device information associated with the user.
     */
    private String device;

    /**
     * The event ID associated with the user.
     */
    private String eventID;

    /**
     * The status of the user (e.g., confirmed, pending).
     */
    private String status;

    /**
     * Whether the user is confirmed.
     */
    private boolean isConfirmed;

    /**
     * The latitude of the user's location.
     */
    private double latitude;

    /**
     * The longitude of the user's location.
     */
    private double longitude;

    /**
     * Default constructor for the User class.
     * This constructor is used by Firestore for deserialization.
     */
    public User() {}

    /**
     * Constructor to initialize mandatory fields for a user.
     *
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email address of the user.
     * @param contactInfo The contact information of the user.
     * @param device The device information of the user.
     */
    public User(String firstName, String lastName, String email, String contactInfo, String device) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactInfo = contactInfo;
        this.device = device;
    }

    /**
     * Constructor for waiting list users with only userID, first name, and last name.
     *
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param userID The unique ID of the user.
     */
    public User(String firstName, String lastName, String userID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
    }

    /**
     * Constructor for a user with latitude and longitude.
     *
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param latitude The latitude of the user's location.
     * @param longitude The longitude of the user's location.
     */
    public User(String firstName, String lastName, double latitude, double longitude) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the first name of the user.
     *
     * @return The user's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The user's first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return The user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The user's last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The user's email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the contact information of the user.
     *
     * @return The user's contact information.
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the contact information of the user.
     *
     * @param contactInfo The user's contact information.
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Gets the avatar URL for the user's profile picture.
     *
     * @return The avatar URL.
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Sets the avatar URL for the user's profile picture.
     *
     * @param avatarUrl The avatar URL.
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * Gets the unique user ID.
     *
     * @return The user ID.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the unique user ID.
     *
     * @param userID The user ID.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Gets the device information associated with the user.
     *
     * @return The device information.
     */
    public String getDevice() {
        return device;
    }

    /**
     * Sets the device information for the user.
     *
     * @param device The device information.
     */
    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * Gets the event ID associated with the user.
     *
     * @return The event ID.
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the event ID for the user.
     *
     * @param eventID The event ID.
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Gets the status of the user (e.g., confirmed, pending).
     *
     * @return The user's status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the user.
     *
     * @param status The user's status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the latitude of the user's location.
     *
     * @return The user's latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the user's location.
     *
     * @param latitude The user's latitude.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the user's location.
     *
     * @return The user's longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the user's location.
     *
     * @param longitude The user's longitude.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Utility method to get the full name of the user.
     *
     * @return The full name of the user (first name + last name).
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Returns a string representation of the user object.
     *
     * @return A string representing the user.
     */
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
                ", eventID='" + eventID + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

package com.example.vortex_app.model;

import java.util.Date;

/**
 * {@code NotificationModel} represents a model for notifications in the Vortex app.
 * It holds the information related to a notification, including the title, message, status,
 * unique identifier (Firestore document ID), and timestamp.
 *
 * <p>This model is used to store and retrieve details of each notification,
 * allowing easy access to notification attributes for display and management.
 */
public class NotificationModel {

    private String title;
    private String message;
    private String status;
    private String id; // Firestore document ID
    private Date TimeStamp;
    private String eventID;
    private String userID;

    /**
     * Constructs a {@code NotificationModel} instance with the specified parameters.
     *
     * @param title     The title of the notification.
     * @param message   The message body of the notification.
     * @param status    The status of the notification, indicating its state (e.g., read, unread).
     * @param id        The Firestore document ID associated with the notification.
     * @param Timestamp The timestamp indicating when the notification was created or last updated.
     */
    public NotificationModel(String title, String message, String status, String id, Date Timestamp, String eventID, String userID) {
        this.title = title;
        this.message = message;
        this.status = status;
        this.id = id;
        this.TimeStamp = Timestamp;
        this.eventID = eventID;
        this.userID = userID;
    }

    /**
     * Returns the title of the notification.
     *
     * @return A {@code String} representing the notification's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the message content of the notification.
     *
     * @return A {@code String} containing the message body of the notification.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the status of the notification.
     *
     * @return A {@code String} indicating the current status of the notification.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the Firestore document ID associated with the notification.
     *
     * @return A {@code String} containing the unique document ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the timestamp of the notification.
     *
     * @return A {@code Date} object representing when the notification was created or last updated.
     */
    public Date getTimeStamp() {
        return TimeStamp;
    }

    public String getEventID() {return eventID;}


    public String getUserID() {return userID;}
}




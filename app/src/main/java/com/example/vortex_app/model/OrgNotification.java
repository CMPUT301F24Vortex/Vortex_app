package com.example.vortex_app.model;

import java.util.Date;

/**
 * Represents a notification for an organizer in the system.
 * This class contains details about a notification such as title, message, and date.
 */
public class OrgNotification {

    /**
     * The unique ID for the notification.
     */
    private String notificationId;

    /**
     * The title of the notification.
     */
    private String title;

    /**
     * The content or message of the notification.
     */
    private String message;

    /**
     * The date when the notification was created or sent.
     */
    private Date date;

    /**
     * Constructor to initialize an OrgNotification with all fields.
     *
     * @param title The title of the notification.
     * @param message The content of the notification.
     * @param date The date when the notification was created or sent.
     * @param notificationId The unique ID of the notification.
     */
    public OrgNotification(String title, String message, Date date, String notificationId) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.notificationId = notificationId;
    }

    /**
     * Gets the unique ID of the notification.
     *
     * @return The notification ID.
     */
    public String getNotificationId() {
        return notificationId;
    }

    /**
     * Gets the title of the notification.
     *
     * @return The title of the notification.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the content or message of the notification.
     *
     * @return The message of the notification.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the date when the notification was created or sent.
     *
     * @return The date of the notification.
     */
    public Date getDate() {
        return date;
    }
}

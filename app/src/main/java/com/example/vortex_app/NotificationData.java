package com.example.vortex_app;

public class NotificationData {
    private String title;
    private String message;
    private String eventID;
    private String userID;
    private int iconResId;
    private String type;

    public NotificationData(String title, String message, String eventID, String userID, int iconResId, String type) {
        this.title = title;
        this.message = message;
        this.eventID = eventID;
        this.userID = userID;
        this.iconResId = iconResId;
        this.type = type;
    }

    // Getters and setters...

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getEventID() { return eventID; }
    public String getUserID() { return userID; }
    public int getIconResId() { return iconResId; }
    public String getType() { return type; }
}

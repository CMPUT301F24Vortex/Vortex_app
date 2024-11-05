package com.example.vortex_app;

public class NotificationModel {
    private String title;
    private String message;
    private String status;
    private String id; // Firestore document ID

    public NotificationModel(String title, String message, String status, String id) {
        this.title = title;
        this.message = message;
        this.status = status;
        this.id = id;
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public String getId() { return id; }
}

package com.example.vortex_app;
import java.util.Date;

public class NotificationModel {
    private String title;
    private String message;
    private String status;
    private String id;// Firestore document ID
    private Date TimeStamp;

    public NotificationModel(String title, String message, String status, String id, Date Timestamp) {
        this.title = title;
        this.message = message;
        this.status = status;
        this.id = id;
        this.TimeStamp = Timestamp;
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public String getId() { return id; }
    public Date getTimeStamp() { return TimeStamp; }

}

package com.example.vortex_app.model;

import java.util.Date;

public class OrgNotification {
    private String title;
    private String message;
    private Date date;

    public OrgNotification(String title, String message, Date date) {
        this.title = title;
        this.message = message;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}
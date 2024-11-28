package com.example.vortex_app.model;

public class orgList {
    private String userID;
    private String eventID;

    public orgList() {

    }

    public orgList(String userID, String eventID) {
        this.userID = userID;
        this.eventID = eventID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}

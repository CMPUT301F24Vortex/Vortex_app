package com.example.vortex_app;

public class Facility {

    private String facilityID;
    private String organizerID;
    private String location;
    private String imageURL;

    // Getters for facility class
    public String getFacilityID(){return this.facilityID;}
    public String getOrganizerID() {return organizerID;}
    public String getLocation() {return location;}
    public String getImageURL() {return imageURL;}

    // setters for Facility class

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }
}

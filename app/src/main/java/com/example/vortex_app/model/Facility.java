package com.example.vortex_app.model;

public class Facility {
    private String id; // Firestore document ID
    private String facilityName;
    private String address;

    // No-arg constructor required for Firestore deserialization
    public Facility() {}

    // Constructor with all fields
    public Facility(String id, String facilityName, String address) {
        this.id = id;
        this.facilityName = facilityName;
        this.address = address;
    }

    public Facility(String facilityName, String address) {
        this.facilityName = facilityName;
        this.address = address;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

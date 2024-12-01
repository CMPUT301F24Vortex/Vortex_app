package com.example.vortex_app.model;

public class Entrant {
    private String firstName;
    private String lastName;
    private double latitude;
    private double longitude;

    public Entrant(String firstName, String lastName, double latitude, double longitude) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

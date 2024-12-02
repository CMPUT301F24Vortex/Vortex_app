package com.example.vortex_app.model;

/**
 * Represents an entrant with personal and location information.
 * This class stores the entrant's first name, last name, and location coordinates (latitude and longitude).
 */
public class Entrant {

    private String firstName;
    private String lastName;
    private double latitude;
    private double longitude;

    /**
     * Constructs an Entrant object with the given first name, last name, latitude, and longitude.
     *
     * @param firstName the first name of the entrant
     * @param lastName the last name of the entrant
     * @param latitude the latitude coordinate of the entrant's location
     * @param longitude the longitude coordinate of the entrant's location
     */
    public Entrant(String firstName, String lastName, double latitude, double longitude) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the first name of the entrant.
     *
     * @return the first name of the entrant
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the entrant.
     *
     * @return the last name of the entrant
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the latitude coordinate of the entrant's location.
     *
     * @return the latitude of the entrant's location
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude coordinate of the entrant's location.
     *
     * @return the longitude of the entrant's location
     */
    public double getLongitude() {
        return longitude;
    }
}

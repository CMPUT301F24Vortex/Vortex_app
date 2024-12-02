package com.example.vortex_app.model;

/**
 * Represents a facility in the system.
 * This class is used for deserializing data from Firebase Firestore.
 */
public class Facility {

    /**
     * Firestore document ID for the facility.
     */
    private String id;

    /**
     * The name of the facility.
     */
    private String facilityName;

    /**
     * The address of the facility.
     */
    private String address;

    /**
     * The ID of the organizer associated with the facility.
     */
    private String organizerID;

    /**
     * No-argument constructor required for Firestore deserialization.
     */
    public Facility() {}

    /**
     * Constructor to initialize a facility with all fields.
     *
     * @param id The Firestore document ID of the facility.
     * @param facilityName The name of the facility.
     * @param address The address of the facility.
     */
    public Facility(String id, String facilityName, String address) {
        this.id = id;
        this.facilityName = facilityName;
        this.address = address;
    }

    /**
     * Constructor to initialize a facility with the name and address.
     *
     * @param facilityName The name of the facility.
     * @param address The address of the facility.
     */
    public Facility(String facilityName, String address) {
        this.facilityName = facilityName;
        this.address = address;
    }

    /**
     * Gets the Firestore document ID of the facility.
     *
     * @return The Firestore document ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the Firestore document ID of the facility.
     *
     * @param id The Firestore document ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the facility.
     *
     * @return The name of the facility.
     */
    public String getFacilityName() {
        return facilityName;
    }

    /**
     * Sets the name of the facility.
     *
     * @param facilityName The name of the facility.
     */
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    /**
     * Gets the address of the facility.
     *
     * @return The address of the facility.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the facility.
     *
     * @param address The address of the facility.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the ID of the organizer associated with the facility.
     *
     * @return The organizer ID.
     */
    public String getOrganizerID() {
        return organizerID;
    }
}

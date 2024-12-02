package com.example.vortex_app.model;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.List;

/**
 * Represents an event with various details such as name, location, time, price, and more.
 * This class is used to map data from Firestore to an Event object for further manipulation.
 */
public class Event implements Serializable {

    @PropertyName("eventName")
    private String name;

    private String imageResId; // Kept as String since it's stored as String in Firestore
    private List<String> classDays; // List of Strings
    private String time;
    private String period;
    private String registrationDueDate;
    private String registrationOpenDate;
    private String price; // Store as String to match Firestore
    private String location;
    private String maxPeople; // Stored as String in Firestore
    private String difficulty;
    private String requiresGeolocation; // Stored as String
    private String eventID;
    private String facilityName;
    private String organizerID;
    private String imageUrl;
    private String userID;
    private String qrCode;

    /**
     * No-argument constructor required for Firestore deserialization.
     */
    public Event() {
    }

    /**
     * Gets the name of the event.
     *
     * @return the name of the event
     */
    @PropertyName("eventName")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the event.
     *
     * @param name the name to set for the event
     */
    @PropertyName("eventName")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the image resource ID of the event.
     *
     * @return the image resource ID of the event
     */
    public String getImageResId() {
        return imageResId;
    }

    /**
     * Sets the image resource ID of the event.
     *
     * @param imageResId the image resource ID to set
     */
    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    /**
     * Gets the list of class days for the event.
     *
     * @return the list of class days
     */
    public List<String> getClassDays() {
        return classDays;
    }

    /**
     * Sets the list of class days for the event.
     *
     * @param classDays the list of class days to set
     */
    public void setClassDays(List<String> classDays) {
        this.classDays = classDays;
    }

    /**
     * Gets the time of the event.
     *
     * @return the time of the event
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time of the event.
     *
     * @param time the time to set for the event
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Gets the period of the event (e.g., morning, afternoon).
     *
     * @return the period of the event
     */
    public String getPeriod() {
        return period;
    }

    /**
     * Sets the period of the event.
     *
     * @param period the period to set for the event
     */
    public void setPeriod(String period) {
        this.period = period;
    }

    /**
     * Gets the registration due date for the event.
     *
     * @return the registration due date
     */
    public String getRegistrationDueDate() {
        return registrationDueDate;
    }

    /**
     * Sets the registration due date for the event.
     *
     * @param registrationDueDate the registration due date to set
     */
    public void setRegistrationDueDate(String registrationDueDate) {
        this.registrationDueDate = registrationDueDate;
    }

    /**
     * Gets the registration open date for the event.
     *
     * @return the registration open date
     */
    public String getRegistrationOpenDate() {
        return registrationOpenDate;
    }

    /**
     * Sets the registration open date for the event.
     *
     * @param registrationOpenDate the registration open date to set
     */
    public void setRegistrationOpenDate(String registrationOpenDate) {
        this.registrationOpenDate = registrationOpenDate;
    }

    /**
     * Gets the price of the event.
     *
     * @return the price of the event
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets the price of the event.
     *
     * @param price the price to set for the event
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Gets the location of the event.
     *
     * @return the location of the event
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the event.
     *
     * @param location the location to set for the event
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the maximum number of people for the event.
     *
     * @return the maximum number of people
     */
    public String getMaxPeople() {
        return maxPeople;
    }

    /**
     * Sets the maximum number of people for the event.
     *
     * @param maxPeople the maximum number of people to set
     */
    public void setMaxPeople(String maxPeople) {
        this.maxPeople = maxPeople;
    }

    /**
     * Gets the difficulty of the event.
     *
     * @return the difficulty of the event
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty of the event.
     *
     * @param difficulty the difficulty to set for the event
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets whether the event requires geolocation.
     *
     * @return the geolocation requirement status
     */
    public String getRequiresGeolocation() {
        return requiresGeolocation;
    }

    /**
     * Sets whether the event requires geolocation.
     *
     * @param requiresGeolocation the geolocation requirement status to set
     */
    public void setRequiresGeolocation(String requiresGeolocation) {
        this.requiresGeolocation = requiresGeolocation;
    }

    /**
     * Gets the unique event ID.
     *
     * @return the event ID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the unique event ID.
     *
     * @param eventID the event ID to set
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Gets the name of the facility hosting the event.
     *
     * @return the facility name
     */
    public String getFacilityName() {
        return facilityName;
    }

    /**
     * Sets the name of the facility hosting the event.
     *
     * @param facilityName the facility name to set
     */
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    /**
     * Gets the ID of the organizer.
     *
     * @return the organizer ID
     */
    public String getOrganizerID() {
        return organizerID;
    }

    /**
     * Sets the ID of the organizer.
     *
     * @param organizerID the organizer ID to set
     */
    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }

    /**
     * Gets the image URL for the event.
     *
     * @return the image URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image URL for the event.
     *
     * @param imageUrl the image URL to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the user ID associated with the event.
     *
     * @return the user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the user ID associated with the event.
     *
     * @param userID the user ID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Gets the QR code for the event.
     *
     * @return the QR code
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * Sets the QR code for the event.
     *
     * @param qrCode the QR code to set
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}

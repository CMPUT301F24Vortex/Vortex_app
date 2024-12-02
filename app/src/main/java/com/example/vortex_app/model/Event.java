package com.example.vortex_app.model;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.List;

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

    // No-argument constructor (required for Firestore deserialization)
    public Event() {
    }

    // Getters and Setters
    @PropertyName("eventName")
    public String getName() {
        return name;
    }

    @PropertyName("eventName")
    public void setName(String name) {
        this.name = name;
    }

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    public List<String> getClassDays() {
        return classDays;
    }

    public void setClassDays(List<String> classDays) {
        this.classDays = classDays;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getRegistrationDueDate() {
        return registrationDueDate;
    }

    public void setRegistrationDueDate(String registrationDueDate) {
        this.registrationDueDate = registrationDueDate;
    }

    public String getRegistrationOpenDate() {
        return registrationOpenDate;
    }

    public void setRegistrationOpenDate(String registrationOpenDate) {
        this.registrationOpenDate = registrationOpenDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(String maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getRequiresGeolocation() {
        return requiresGeolocation;
    }

    public void setRequiresGeolocation(String requiresGeolocation) {
        this.requiresGeolocation = requiresGeolocation;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getOrganizerID() {
        return organizerID;
    }

    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    // Add getters and setters for qrCode
    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}

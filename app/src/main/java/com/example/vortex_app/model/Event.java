package com.example.vortex_app.model;

import java.io.Serializable;

public class Event implements Serializable {

    private String name;
    private int imageResId; // Optional: For displaying an image in the UI
    private String classDay;
    private String time;
    private String period;
    private String registrationDueDate;
    private String registrationOpenDate;
    private double price;
    private String location;
    private int maxPeople;
    private String difficulty;
    private boolean requiresGeolocation;
    private String eventID;

    // Full constructor with all parameters
    public Event(String name, int imageResId, String classDay, String time, String period,
                 String registrationDueDate, String registrationOpenDate, String price,
                 String location, int maxPeople, String difficulty, boolean requiresGeolocation) {
        this.name = name;
        this.imageResId = imageResId;
        this.classDay = classDay;
        this.time = time;
        this.period = period;
        this.registrationDueDate = registrationDueDate;
        this.registrationOpenDate = registrationOpenDate;
        this.price = parsePrice(price); // Parse price as a double
        this.location = location;
        this.maxPeople = maxPeople;
        this.difficulty = difficulty;
        this.requiresGeolocation = requiresGeolocation;
    }

    // Constructor for minimal event (e.g., name and eventID only)
    public Event(String name, String eventID) {
        this.name = name;
        this.eventID = eventID;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getClassDay() {
        return classDay;
    }

    public void setClassDay(String classDay) {
        this.classDay = classDay;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPrice(String price) {
        this.price = parsePrice(price);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public boolean isRequiresGeolocation() {
        return requiresGeolocation;
    }

    public void setRequiresGeolocation(boolean requiresGeolocation) {
        this.requiresGeolocation = requiresGeolocation;
    }

    // Utility Method: Parse Price
    private double parsePrice(String price) {
        try {
            return Double.parseDouble(price);
        } catch (NumberFormatException e) {
            return 0.0; // Default value for invalid price input
        }
    }
}

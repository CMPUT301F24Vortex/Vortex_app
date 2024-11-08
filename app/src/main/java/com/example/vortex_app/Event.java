package com.example.vortex_app;

import java.io.Serializable;

/**
 * The Event class represents an event with various attributes such as name, image resource,
 * date and time information, location, price, difficulty, and other related details.
 * It implements Serializable to allow object serialization.
 */
public class Event implements Serializable {
    private String name;
    private int imageResId;
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

    /**
     * Constructs an Event object with specified details.
     *
     * @param name                  The name of the event.
     * @param imageResId            The resource ID for the event's image.
     * @param classDay              The day(s) the event takes place.
     * @param time                  The time of the event.
     * @param period                The period or duration of the event.
     * @param registrationDueDate   The due date for registration.
     * @param registrationOpenDate  The date when registration opens.
     * @param price                 The price of the event (converted to a double).
     * @param location              The location of the event.
     * @param maxPeople             The maximum number of people allowed for the event.
     * @param difficulty            The difficulty level of the event.
     * @param requiresGeolocation   Whether the event requires geolocation.
     */
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
        this.price = Double.parseDouble(price);
        this.location = location;
        this.maxPeople = maxPeople;
        this.difficulty = difficulty;
        this.requiresGeolocation = requiresGeolocation;
    }

    // Getters for each field

    /**
     * Gets the name of the event.
     *
     * @return The name of the event.
     */
    public String getName() { return name; }

    /**
     * Gets the image resource ID associated with the event.
     *
     * @return The image resource ID.
     */
    public int getImageResId() { return imageResId; }

    /**
     * Gets the day(s) the event takes place.
     *
     * @return The day(s) of the event.
     */
    public String getClassDay() { return classDay; }

    /**
     * Gets the time of the event.
     *
     * @return The event time.
     */
    public String getTime() { return time; }

    /**
     * Gets the period or duration of the event.
     *
     * @return The event period.
     */
    public String getPeriod() { return period; }

    /**
     * Gets the registration due date.
     *
     * @return The registration due date.
     */
    public String getRegistrationDueDate() { return registrationDueDate; }

    /**
     * Gets the registration open date.
     *
     * @return The registration open date.
     */
    public String getRegistrationOpenDate() { return registrationOpenDate; }

    /**
     * Gets the price of the event.
     *
     * @return The event price.
     */
    public double getPrice() { return price; }

    /**
     * Gets the location of the event.
     *
     * @return The event location.
     */
    public String getLocation() { return location; }

    /**
     * Gets the maximum number of people allowed for the event.
     *
     * @return The maximum number of people.
     */
    public int getMaxPeople() { return maxPeople; }

    /**
     * Gets the difficulty level of the event.
     *
     * @return The event difficulty.
     */
    public String getDifficulty() { return difficulty; }

    /**
     * Indicates whether the event requires geolocation.
     *
     * @return True if the event requires geolocation, otherwise false.
     */
    public boolean isRequiresGeolocation() { return requiresGeolocation; }

    /**
     * Gets the unique event ID.
     *
     * @return The event ID.
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the unique event ID.
     *
     * @param eventID The event ID to set.
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}

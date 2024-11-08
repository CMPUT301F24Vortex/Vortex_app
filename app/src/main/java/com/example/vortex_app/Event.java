package com.example.vortex_app;

import java.io.Serializable;

/**
 * {@code Event} represents an event with various attributes, including name, schedule, location,
 * registration details, and geolocation requirements. This class provides a model for managing
 * event data within the application.
 */
public class Event {

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
     * Constructs an {@code Event} with all attributes specified.
     *
     * @param name                 The name of the event.
     * @param imageResId           The resource ID of the event image.
     * @param classDay             The day the class or event is held.
     * @param time                 The time the event is held.
     * @param period               The duration or period of the event.
     * @param registrationDueDate  The registration due date for the event.
     * @param registrationOpenDate The registration open date for the event.
     * @param price                The price of the event as a string, which is parsed to a double.
     * @param location             The location of the event.
     * @param maxPeople            The maximum number of participants allowed.
     * @param difficulty           The difficulty level of the event.
     * @param requiresGeolocation  Whether the event requires geolocation verification.
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

    /**
     * Constructs an {@code Event} with only name and event ID.
     * This constructor can be used for scenarios where minimal event information is sufficient.
     *
     * @param name    The name of the event.
     * @param eventID The unique identifier for the event.
     */
    public Event(String name, String eventID) {
        this.name = name;
        this.eventID = eventID;
    }

    // Getters for each field

    /** @return The name of the event. */
    public String getName() { return name; }

    /** @return The resource ID of the event image. */
    public int getImageResId() { return imageResId; }

    /** @return The day the class or event is held. */
    public String getClassDay() { return classDay; }

    /** @return The time the event is held. */
    public String getTime() { return time; }

    /** @return The duration or period of the event. */
    public String getPeriod() { return period; }

    /** @return The registration due date for the event. */
    public String getRegistrationDueDate() { return registrationDueDate; }

    /** @return The registration open date for the event. */
    public String getRegistrationOpenDate() { return registrationOpenDate; }

    /** @return The price of the event as a double. */
    public double getPrice() { return price; }

    /** @return The location of the event. */
    public String getLocation() { return location; }

    /** @return The maximum number of participants allowed. */
    public int getMaxPeople() { return maxPeople; }

    /** @return The difficulty level of the event. */
    public String getDifficulty() { return difficulty; }

    /** @return The unique identifier of the event. */
    public String getEventID() { return eventID; }

    /** @return {@code true} if the event requires geolocation verification, {@code false} otherwise. */
    public boolean isRequiresGeolocation() { return requiresGeolocation; }
}

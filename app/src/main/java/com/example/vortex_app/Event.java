package com.example.vortex_app;
import java.io.Serializable;

public class Event implements Serializable{
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

    // Constructor with all parameters
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
    public String getName() { return name; }
    public int getImageResId() { return imageResId; }
    public String getClassDay() { return classDay; }
    public String getTime() { return time; }
    public String getPeriod() { return period; }
    public String getRegistrationDueDate() { return registrationDueDate; }
    public String getRegistrationOpenDate() { return registrationOpenDate; }
    public double getPrice() { return price; }
    public String getLocation() { return location; }
    public int getMaxPeople() { return maxPeople; }
    public String getDifficulty() { return difficulty; }
    public boolean isRequiresGeolocation() { return requiresGeolocation; }
}


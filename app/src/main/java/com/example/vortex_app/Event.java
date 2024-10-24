package com.example.vortex_app;

public class Event {
    private String name;
    private int imageResId;
    private String classDay;
    private String time;
    private String period;
    private String registrationDueDate;
    private String registrationOpenDate;
    private String price;
    private String location;
    private int maxPeople;
    private String difficulty;

    // Full constructor with all the parameters
    public Event(String name, int imageResId, String classDay, String time, String period,
                 String registrationDueDate, String registrationOpenDate, String price,
                 String location, int maxPeople, String difficulty) {
        this.name = name;
        this.imageResId = imageResId;
        this.classDay = classDay;
        this.time = time;
        this.period = period;
        this.registrationDueDate = registrationDueDate;
        this.registrationOpenDate = registrationOpenDate;
        this.price = price;
        this.location = location;
        this.maxPeople = maxPeople;
        this.difficulty = difficulty;
    }

    // Getters for each field
    public String getName() { return name; }
    public int getImageResId() { return imageResId; }
    public String getClassDay() { return classDay; }
    public String getTime() { return time; }
    public String getPeriod() { return period; }
    public String getRegistrationDueDate() { return registrationDueDate; }
    public String getRegistrationOpenDate() { return registrationOpenDate; }
    public String getPrice() { return price; }
    public String getLocation() { return location; }
    public int getMaxPeople() { return maxPeople; }
    public String getDifficulty() { return difficulty; }
}

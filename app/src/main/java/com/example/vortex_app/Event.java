package com.example.vortex_app;

import android.media.Image;

import java.util.List;

public class Event {

    private String organizerID;
    private String posterURL;
    private String qrCodeData;
    private String facility;
    private boolean geoLocReq;
    private String time;        // 24hr eg: 17:30
    private float price;
    private int maxEntrants;
    private String name;
    private String regStartDate;
    private String regEndDate;

    private boolean recurring;    // signifies a one time event (false) or a repeating event (true)
    // use if one time event
    private String date;           // yyyy-mm-dd

    // use if event is recurring
    private String startDate;   // yyyy-mm-dd
    private String endDate;     // yyyy-mm-dd
    private List<String> days;  // mon, tue, fri etc.


    // Getters for Event class
    public String getOrganizerID() {return this.organizerID;}
    public String getPosterURL () {return this.posterURL;}
    public String getQrCodeData () {return this.qrCodeData;}
    public String getFacility () {return this.facility;}
    public Boolean getGeoLocReq() {return this.geoLocReq;}
    public String getTime() {return this.time;}
    public float getPrice() {return this.price;}
    public int getMaxEntrants() {return this.maxEntrants;}
    public String getName() {return this.name;}
    public String getRegStartDate() {return this.regStartDate;}
    public String getRegEndDate() {return this.regEndDate;}
    public Boolean getRecurring() {return this.recurring;}
    public String getDate () {return this.date;}
    public String getStartDate() {return this.startDate;}
    public String getEndDate() {return this.endDate;}
    public List<String> getDays() {return this.days;}


    // Setters for event class
    public void setOrganizerID(String newOrganizerID) {
        this.organizerID = newOrganizerID;
    }
    public void setPosterURL(String newPosterURL) {
        this.posterURL = newPosterURL;
    }
    public void setQrCodeData(String newQrCodeData) {
        this.qrCodeData = newQrCodeData;
    }
    public void setFacility(String newFacility) {
        this.facility = newFacility;
    }
    public void setGeoLocReq(Boolean newGeoLocReq) {
        this.geoLocReq = newGeoLocReq;
    }
    public void setTime(String newTime) {
        this.time = newTime;
    }
    public void setPrice(float newPrice) {
        this.price = newPrice;
    }
    public void setMaxEntrants(int newMaxEntrants) {
        this.maxEntrants = newMaxEntrants;
    }
    public void setName(String newName) {
        this.name = newName;
    }
    public void setRegStartDate(String newRegStartDate) {
        this.regStartDate = newRegStartDate;
    }
    public void setRegEndDate(String newRegEndDate) {
        this.regEndDate = newRegEndDate;
    }
    public void setRecurring(Boolean newRecurring){
        this.recurring = newRecurring;
    }
    public void setDate(String newDate) {
        this.date = newDate;
    }
    public void setStartDate(String newStartDate){
        this.startDate = newStartDate;
    }
    public void setEndDate(String newEndDate){
        this.endDate = newEndDate;
    }
    public void setDays(List<String> days) {
        this.days = days;
    }
}

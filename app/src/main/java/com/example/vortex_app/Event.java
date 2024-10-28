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



    public String getOrganizerID() {return this.organizerID;}
    public String getPosterURL () {return this.posterURL;}
    public String getQrCodeData () {return this.qrCodeData;}
    public String getFacility () {return this.facility;}
    public Boolean getGeoLocReq() {return this.geoLocReq;}
    public String getTime() {return this.time;}
    public 



}

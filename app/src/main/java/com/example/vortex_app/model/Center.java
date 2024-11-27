package com.example.vortex_app.model;

public class Center {
    private String name;
    private String address;
    private String facilityID;

    public Center(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Center () {}

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
    public String getFacilityID() {return facilityID;}
}

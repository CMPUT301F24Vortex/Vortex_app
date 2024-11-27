package com.example.vortex_app.model;

public class Center {
    private String name;
    private String address;

    public Center(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}

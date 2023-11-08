/*
Class: DBHandler.java
Date: Nov 8th 2023
Programmer: Sarah Wedge
Description:

 */

package com.example.assignment2;

public class Location {

    private int id;
    private String address, latitude, longitude;

    public Location(int id, String address, String latitude, String longitude) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }
}

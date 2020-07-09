package com.example.restauranthealthinspectionbrowser.model;

/**
 * Restaurant class stores information about a single restaurant. Data include
 * ID, name, address and GPS coordinates.
 */
public class Restaurant implements Comparable<Restaurant> {
    private String mID;
    private String mName;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;

    public Restaurant() {
    }

    public String getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    @Override
    public int compareTo(Restaurant o) {
        return getName().compareTo(o.getName());
    }
}

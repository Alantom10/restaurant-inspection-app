package com.example.restauranthealthinspectionbrowser.model;

/**
 * Restaurant class stores information about a single restaurant. Data include
 * ID, name, address and GPS coordinates.
 */
public class Restaurant implements Comparable<Restaurant> {
    private String mId;
    private String mTitle;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;

    public Restaurant() {
    }

    public Restaurant(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
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

    public void setId(String id) {
        mId = id;
    }

    public void setTitle(String title) {
        mTitle = title;
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
        return getTitle().compareTo(o.getTitle());
    }
}

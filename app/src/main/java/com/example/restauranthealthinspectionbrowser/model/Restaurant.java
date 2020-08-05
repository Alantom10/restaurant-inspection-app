package com.example.restauranthealthinspectionbrowser.model;

import java.util.Date;

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
    private int mIssues;
    private String mRating;
    private Date mDate;
    private boolean mIsFavorite;

    public Restaurant() {
    }


    public Restaurant(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public int getIssues() {
        return mIssues;
    }

    public void setIssues(int issues) {
        mIssues = issues;
    }

    public String getRating() {
        return mRating;
    }

    public void setRating(String rating) {
        mRating = rating;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(boolean mIsFavorite) {
        this.mIsFavorite = mIsFavorite;
    }
    @Override
    public int compareTo(Restaurant o) {
        return getTitle().compareTo(o.getTitle());
    }
}

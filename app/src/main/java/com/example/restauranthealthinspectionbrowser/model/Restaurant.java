package com.example.restauranthealthinspectionbrowser.model;

public class Restaurant {
    private String mID;
    private String mName;
    private String mAddress;

    public Restaurant() {
        mID = "SDFO-8HKP7E";
        mName = "Pattullo A&W";
        mAddress = "12808 King George Blvd, Surrey";
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
}

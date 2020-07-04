package com.example.restauranthealthinspectionbrowser.model;

public class Restaurant {
    private String mID;
    private String mName;
    private String mAddress;

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

    public void setID(String ID) {
        mID = ID;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setAddress(String address) {
        mAddress = address;
    }
}

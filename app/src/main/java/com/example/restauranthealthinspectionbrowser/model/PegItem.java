package com.example.restauranthealthinspectionbrowser.model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class PegItem implements ClusterItem{
    private LatLng mPosition;
    private String mTitle;
    private String mHazardLevel;

    public PegItem(double lat, double lng, String mTitle, String mHazardLevel) {
        this.mPosition = new LatLng(lat, lng);
        this.mTitle = mTitle;
        this.mHazardLevel = mHazardLevel;
    }

    public PegItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return "";
    }

    public String getmHazardLevel() {
        return mHazardLevel;
    }

}

package com.example.restauranthealthinspectionbrowser.model;

public class Inspection {
    private String mInspectionDate;
    private String mHazardRating;

    public Inspection(String inspectionDate, String hazardRating) {
        mInspectionDate = inspectionDate;
        mHazardRating = hazardRating;
    }

    public String getInspectionDate() {
        return mInspectionDate;
    }

    public String getHazardRating() {
        return mHazardRating;
    }

    public void setInspectionDate(String inspectionDate) {
        mInspectionDate = inspectionDate;
    }

    public void setHazardRating(String hazardRating) {
        mHazardRating = hazardRating;
    }
}

package com.example.restauranthealthinspectionbrowser.model;

public class Inspection {
    private String mInspectionDate;

    public Inspection(String inspectionDate) {
        mInspectionDate = inspectionDate;
    }

    public String getInspectionDate() {
        return mInspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        mInspectionDate = inspectionDate;
    }
}

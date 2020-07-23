package com.example.restauranthealthinspectionbrowser.model;

/**
 * Inspection class stores information about a single violation. Data include
 * the index and a description.
 */
public class Violation {
    private int mIndex;
    private String mViolationDescription;

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public String getViolationDescription() {
        return mViolationDescription;
    }

    public void setViolationDescription(String violationDescription) {
        this.mViolationDescription = violationDescription;
    }
}

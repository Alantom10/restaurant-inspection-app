package com.example.restauranthealthinspectionbrowser.model;

/**
 * Inspection class stores information about a single violation. Data include
 * the index and a description.
 */
public class Violation {
    private int index;
    private String violationBriefDesc;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getViolationBriefDesc() {
        return violationBriefDesc;
    }

    public void setViolationBriefDesc(String violationBriefDesc) {
        this.violationBriefDesc = violationBriefDesc;
    }
}

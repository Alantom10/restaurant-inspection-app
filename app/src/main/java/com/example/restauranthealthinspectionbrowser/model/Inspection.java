package com.example.restauranthealthinspectionbrowser.model;

import java.util.Date;

/**
 * Inspection class stores information about a single inspection. Data include
 * tracking number, date, type, hazard rating, number of issues, and associated
 * violations.
 */
public class Inspection implements Comparable<Inspection> {
    private String trackingNum;
    private Date inspectionDate;
    private String inspectionType;
    private String hazardRating;
    private int numOfCritical;
    private int numOfNonCritical;
    private String[] violations;

    public String getTrackingNum() {
        return trackingNum;
    }

    public void setTrackingNum(String trackingNum) {
        this.trackingNum = trackingNum;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(String inspectionType) {
        this.inspectionType = inspectionType;
    }

    public String getHazardRating() {
        return hazardRating;
    }

    public void setHazardRating(String hazardRating) {
        this.hazardRating = hazardRating;
    }

    public int getNumOfCritical() {
        return numOfCritical;
    }

    public void setNumOfCritical(int numOfCritical) {
        this.numOfCritical = numOfCritical;
    }

    public int getNumOfNonCritical() {
        return numOfNonCritical;
    }

    public void setNumOfNonCritical(int numOfNonCritical) {
        this.numOfNonCritical = numOfNonCritical;
    }

    public String[] getViolations() {
        return violations;
    }

    public void setViolations(String[] violations) {
        this.violations = violations;
    }

    public Inspection() {

    }

    public Inspection(String trackingNum, Date inspectionDate, String inspectionType,
                      int numOfCritical, int numOfNonCritical,
                      String hazardRating, String[] violations) {
        this.trackingNum = trackingNum;
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.numOfCritical = numOfCritical;
        this.numOfNonCritical = numOfNonCritical;
        this.hazardRating = hazardRating;
    }

    @Override
    public int compareTo(Inspection o) {
        return getInspectionDate().compareTo(o.getInspectionDate());
    }
}

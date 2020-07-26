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
    private int numCritical;
    private int numNonCritical;
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

    public int getNumCritical() {
        return numCritical;
    }

    public void setNumCritical(int numCritical) {
        this.numCritical = numCritical;
    }

    public int getNumNonCritical() {
        return numNonCritical;
    }

    public void setNumNonCritical(int numNonCritical) {
        this.numNonCritical = numNonCritical;
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
                      int numCritical, int numNonCritical,
                      String hazardRating, String[] violations) {
        this.trackingNum = trackingNum;
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.numCritical = numCritical;
        this.numNonCritical = numNonCritical;
        this.hazardRating = hazardRating;
    }

    @Override
    public int compareTo(Inspection o) {
        return getInspectionDate().compareTo(o.getInspectionDate());
    }
}

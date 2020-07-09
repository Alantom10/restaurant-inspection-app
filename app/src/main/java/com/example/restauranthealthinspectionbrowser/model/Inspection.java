package com.example.restauranthealthinspectionbrowser.model;

import java.util.Date;

public class Inspection implements Comparable<Inspection> {
    private String trackingNum;
    private Date inspectionDate;
    private String inspectionType;
    private String hazardRating;
    private int numOfCritical;
    private int numOfNonCritical;
    private String[] violation;
    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

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

    public String[] getViolation() {
        return violation;
    }

    public void setViolation(String[] violation) {
        this.violation = violation;
    }

    public Inspection() {

    }

    public Inspection(String trackingNum, Date inspectionDate, String inspectionType,
                      int numOfCritical, int numOfNonCritical,
                      String hazardRating, String violation) {
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

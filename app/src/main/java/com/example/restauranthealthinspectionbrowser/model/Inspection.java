package com.example.restauranthealthinspectionbrowser.model;

import com.example.restauranthealthinspectionbrowser.R;

public class Inspection {
    private String trackingNum;
    private String inspectionDate;
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

    public String getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
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

    public Inspection(String trackingNum, String inspectionDate, String inspectionType,
                      int numOfCritical, int numOfNonCritical,
                      String hazardRating, String violation) {
        this.trackingNum = trackingNum;
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.numOfCritical = numOfCritical;
        this.numOfNonCritical = numOfNonCritical;
        this.hazardRating = hazardRating;
        this.violation = getViolationsToString(violation);
    }

    private String[] getViolationsToString(String violation) {
        return violation.replace(", ", ", ").split("|");
    }

    public int getHazardLogo() {

        if (hazardRating.equals("Low")) {

            return R.drawable.green_face;

        } else if (hazardRating.equals("Moderate")) {

            return R.drawable.yellow_face;

        } else {

            return R.drawable.red_face;

        }

    }

    @Override
    public String toString() {

        return  numOfCritical + ", " +
                numOfNonCritical + ", " +
                //this.date() + ", " +
                inspectionType + ", " +
                hazardRating;

    }
}

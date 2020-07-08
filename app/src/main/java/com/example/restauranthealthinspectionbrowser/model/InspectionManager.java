package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;

import com.example.restauranthealthinspectionbrowser.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class InspectionManager {
//    public static InspectionManager getInstance(){
//        if(instance==null){
//            instance=new InspectionManager();
//        }
//        return instance;
//    }

    private List<Inspection> mInspections;
    private static InspectionManager sInstance;

    public static InspectionManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new InspectionManager(context);
        }
        return sInstance;
    }

    private InspectionManager(Context context) {
        mInspections = new ArrayList<>();
        readData(context);
    }

    public List<Inspection> getInspections(){
        return mInspections;
    }

    public Inspection getLatestInspection(String restaurantID) {

        Inspection latestInspection = getFirstInspection(restaurantID);

        if (latestInspection == null) {
            return null;
        }

        for (Inspection inspection : mInspections) {

            if (inspection.getTrackingNum().equals(restaurantID) &&
            isNewerThan(inspection.getInspectionDate(), latestInspection.getInspectionDate())) {

                latestInspection = inspection;
            }
        }

        return latestInspection;
    }

    public List<Inspection> getAllInspections(String restaurantID) {

        List<Inspection> inspectionList = new ArrayList<Inspection>();

        for (Inspection inspection : mInspections) {

            if (restaurantID.equals(inspection.getTrackingNum())) {
                inspectionList.add(inspection);
            }
        }

        return inspectionList;
    }

    private Inspection getFirstInspection(String restaurantID) {
        for (Inspection inspection: mInspections) {
            if (inspection.getTrackingNum().equals(restaurantID)) {
                return inspection;
            }
        }

        return null;
    }

    private boolean isNewerThan(String inspectionDate1, String inspectionDate2) {

        int inspectionYear1 = Integer.parseInt(inspectionDate1.substring(0,4));
        int inspectionYear2 = Integer.parseInt(inspectionDate2.substring(0,4));

        if (inspectionYear1 > inspectionYear2) {
            return true;
        }

        if (inspectionYear1 < inspectionYear2) {
            return false;
        }

        int inspectionMonth1 = Integer.parseInt(inspectionDate1.substring(4,6));
        int inspectionMonth2 = Integer.parseInt(inspectionDate2.substring(4,6));

        if (inspectionMonth1 > inspectionMonth2) {
            return true;
        }

        if (inspectionMonth1 < inspectionMonth2) {
            return false;
        }

        int inspectionDay1 = Integer.parseInt(inspectionDate1.substring(6,8));
        int inspectionDay2 = Integer.parseInt(inspectionDate2.substring(6,8));

        return inspectionDay1 < inspectionDay2;
    }

    private void readData(Context context){

        try( InputStream is = context.getResources().openRawResource(R.raw.inspectionreports_itr1);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        ){
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                Inspection inspection = new Inspection();
                inspection.setTrackingNum(row[0].replace("\"", ""));
                inspection.setInspectionDate(row[1].replace("\"", ""));
                inspection.setInspectionType(row[2].replace("\"", ""));
                inspection.setNumOfCritical(Integer.valueOf(row[3].replace("\"", "")));
                inspection.setNumOfNonCritical(Integer.valueOf(row[4].replace("\"", "")));
                inspection.setHazardRating(row[5].replace("\"",""));
//                inspection.setTest(row[6]);
                //
                String violations = "";
                for (int i = 6;i< row.length;i++){
                    if(i > 6){
                        violations += ",";
                    }
                    violations += row[i].replace("\"", "");
                }
                String[] vio = violations.split("\\|");
                inspection.setViolation(vio);

                mInspections.add(inspection);
            }
        }
        catch  (IOException e) {
            e.printStackTrace();
        }
    }
}

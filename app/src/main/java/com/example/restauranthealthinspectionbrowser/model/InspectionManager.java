package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;

import com.example.restauranthealthinspectionbrowser.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * InspectionManager class stores a collection of inspections. It supports
 * reading inspection date from file.
 */
public class InspectionManager {
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
        List<Inspection> inspectionList = getInspectionsForRestaurant(restaurantID);

        if (inspectionList.size() == 0) {
            return null;
        }

        return inspectionList.get(0);
    }

    public List<Inspection> getInspectionsForRestaurant(String restaurantID) {
        List<Inspection> inspectionList = new ArrayList<>();
        for (Inspection inspection : mInspections) {
            if (restaurantID.equals(inspection.getTrackingNum())) {
                inspectionList.add(inspection);
            }
        }

        Collections.sort(inspectionList);
        Collections.reverse(inspectionList);

        return inspectionList;
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
                inspection.setInspectionType(row[2].replace("\"", ""));
                inspection.setNumOfCritical(Integer.valueOf(row[3].replace("\"", "")));
                inspection.setNumOfNonCritical(Integer.valueOf(row[4].replace("\"", "")));
                inspection.setHazardRating(row[5].replace("\"",""));

                String inspectionDate = row[1].replace("\"", "");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CANADA);
                Date date = sdf.parse(inspectionDate);
                inspection.setInspectionDate(date);

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
        } catch  (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}

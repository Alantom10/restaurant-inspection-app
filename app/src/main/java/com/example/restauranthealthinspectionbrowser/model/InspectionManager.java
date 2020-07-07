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
import java.util.concurrent.ThreadLocalRandom;

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
        int i = ThreadLocalRandom.current().nextInt(mInspections.size());
        return mInspections.get(i);
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
                    violations += row[i];
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

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

/**
 * RestaurantManager class stores a collection of violations. It supports
 * reading violation date from file.
 */
public class ViolationManager {
    private List<Violation> listBriefViolation = new ArrayList<>();
    private static ViolationManager sInstance;

    public static ViolationManager getInstance(Context context) {
        sInstance = null;
        if (sInstance == null) {
            sInstance = new ViolationManager(context);
        }
        return sInstance;
    }

    public List<Violation> getListBriefViolation() {
        return listBriefViolation;
    }

    private void readData(Context context){
        try(InputStream is = context.getResources().openRawResource(R.raw.violation_description);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        ){
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                Violation briefViolation = new Violation();
                briefViolation.setIndex(Integer.valueOf(row[0].replace("\"", "")));
                briefViolation.setViolationBriefDesc(row[1].replace("\"", ""));
//                inspection.setTest(row[6]);
                //

                listBriefViolation.add(briefViolation);

            }
        }
        catch  (IOException e) {
            e.printStackTrace();
        }
    }

    private ViolationManager(Context context) {
        listBriefViolation = new ArrayList<>();
        readData(context);
    }
}

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
    private List<Violation> mViolations = new ArrayList<>();

    private static ViolationManager sInstance;

    public static ViolationManager getInstance(Context context) {
        sInstance = null;
        if (sInstance == null) {
            sInstance = new ViolationManager(context);
        }
        return sInstance;
    }

    public List<Violation> getViolations() {
        return mViolations;
    }

    private void readData(Context context){
        InputStream is = context.getResources().openRawResource(R.raw.violation_descriptions);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        try {
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                Violation violation = new Violation();
                violation.setIndex(Integer.parseInt(row[0].replace("\"", "")));
                violation.setViolationDescription(row[1].replace("\"", ""));

                mViolations.add(violation);
            }
        }
        catch  (IOException e) {
            e.printStackTrace();
        }
    }

    private ViolationManager(Context context) {
        mViolations = new ArrayList<>();
        readData(context);
    }
}

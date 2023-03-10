package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;
import android.text.format.DateUtils;

import com.example.restauranthealthinspectionbrowser.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.restauranthealthinspectionbrowser.ui.MapsActivity.FILE_NAME_INSPECTION_REPORTS;

/**
 * InspectionManager class stores a collection of inspections. It supports
 * reading inspection date from file.
 */
public class InspectionManager {
    private static final String TAG = "RestaurantManager";

    private List<Inspection> mInspections;

    private static InspectionManager sInstance;

    public static InspectionManager getInstance(Context context)  {
        if (sInstance == null) {
            sInstance = new InspectionManager(context);
        }
        return sInstance;
    }

    private InspectionManager(Context context) {
        mInspections = new ArrayList<>();
        readDataFile(context);
    }

    public Inspection getLatestInspection(String restaurantID) {
        List<Inspection> inspectionList = getInspections(restaurantID);

        if (inspectionList.size() == 0) {
            return null;
        }

        return inspectionList.get(0);
    }

    public int getCriticalIssues(String restaurantID) {
        int criticalIssues = 0;
        long now = System.currentTimeMillis();

        List<Inspection> inspectionList = getInspections(restaurantID);
        for (Inspection inspection : inspectionList) {
            boolean isRecent = now - inspection.getInspectionDate().getTime() < DateUtils.YEAR_IN_MILLIS;
            if (isRecent) {
                criticalIssues += inspection.getNumCritical();
            }
        }

        return criticalIssues;
    }

    public List<Inspection> getInspections(String restaurantID) {
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

    public List<Inspection> getInspections(){
        return mInspections;
    }

    public void updateInspections(Context context) throws FileNotFoundException {
        mInspections.clear();
        readDataFile(context);
    }

    private void readDataFile(Context context)  {
        File file = new File(context.getFilesDir() + "/" + FILE_NAME_INSPECTION_REPORTS);
        InputStream inputStream = null;
        if (file.exists()) {
            try {
                inputStream = context.openFileInput(FILE_NAME_INSPECTION_REPORTS);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            inputStream = context.getResources().openRawResource(R.raw.inspectionreports_itr1);
        }

        if (inputStream == null) {
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        try {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length == 0) {
                    return;
                }

                Inspection inspection = new Inspection();
                inspection.setTrackingNum(row[0].replace("\"", ""));
                inspection.setInspectionType(row[2].replace("\"", ""));
                inspection.setHazardRating(row[row.length - 1].replace("\"",""));
                inspection.setNumCritical(Integer.parseInt(row[3].replace("\"", "")));
                inspection.setNumNonCritical(Integer.parseInt(row[4].replace("\"", "")));
                inspection.setInspectionDate(DateHelper
                        .parseInspectionDate(row[1].replace("\"", "")));

                String violations = "";
                for (int i = 5; i < row.length - 1; i++) {
                    if (i >= 6) {
                        violations += ",";
                    }
                    violations += row[i].replace("\"", "");
                }
                inspection.setViolations(violations.split("\\|"));

                mInspections.add(inspection);
            }
        } catch  (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}

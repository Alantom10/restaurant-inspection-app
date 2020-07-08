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

public class BriefViolationManager {
    private List<BriefViolation> listBriefViolation = new ArrayList<>();
    private static BriefViolationManager sInstance;

    public static BriefViolationManager getInstance(Context context) {
        sInstance = null;
        if (sInstance == null) {
            sInstance = new BriefViolationManager(context);
        }
        return sInstance;
    }

    public List<BriefViolation> getListBriefViolation() {
        return listBriefViolation;
    }

    private void readData(Context context){
        try(InputStream is = context.getResources().openRawResource(R.raw.violation_description);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        ){
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                BriefViolation briefViolation = new BriefViolation();
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

    private BriefViolationManager(Context context) {
        listBriefViolation = new ArrayList<>();
        readData(context);
    }
}

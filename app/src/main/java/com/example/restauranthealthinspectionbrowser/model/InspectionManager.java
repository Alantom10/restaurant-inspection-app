package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class InspectionManager {
    private static InspectionManager sInstance;

    private List<Inspection> mInspections;

    public static InspectionManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new InspectionManager(context);
        }
        return sInstance;
    }

    private InspectionManager(Context context) {
        mInspections = new ArrayList<>();
        mInspections.add(new Inspection("20200702"));
        mInspections.add(new Inspection("20200410"));
        mInspections.add(new Inspection("20190101"));
    }

    public Inspection getLatestInspection(String restaurantID) {
        int i = ThreadLocalRandom.current().nextInt(3);
        return mInspections.get(i);
    }
}

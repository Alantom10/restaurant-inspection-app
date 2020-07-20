package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;
import android.content.SharedPreferences;

public class DataPackageManager {
    private static final String TAG = "DataPackageManager";

    private static final String PREFERENCES = "restaurant list";
    private static final String PREFERENCES_LAST_UPDATED = "last updated";
    private static final String PREFERENCES_LAST_MODIFIED_RESTAURANTS = "last modified restaurants";
    private static final String PREFERENCES_LAST_MODIFIED_INSPECTIONS = "last modified inspections";

    private long mLastUpdated;
    private String mLastModifiedRestaurants;
    private String mLastModifiedInspections;

    private static DataPackageManager sInstance;

    public static DataPackageManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataPackageManager(context);
        }
        return sInstance;
    }

    private DataPackageManager(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        mLastUpdated = sp.getLong(PREFERENCES_LAST_UPDATED, 0);
        mLastModifiedRestaurants = sp.getString(PREFERENCES_LAST_MODIFIED_RESTAURANTS, "");
        mLastModifiedInspections = sp.getString(PREFERENCES_LAST_MODIFIED_INSPECTIONS, "");
    }

    public long getLastUpdated() {
        return mLastUpdated;
    }

    public String getLastModifiedRestaurants() {
        return mLastModifiedRestaurants;
    }

    public String getLastModifiedInspections() {
        return mLastModifiedInspections;
    }

    public void updateLastModified(Context context, String lastModifiedRestaurants, String lastModifiedInspections) {
        mLastModifiedRestaurants = lastModifiedRestaurants;
        mLastModifiedInspections = lastModifiedInspections;
        mLastUpdated = System.currentTimeMillis();

        SharedPreferences sp = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFERENCES_LAST_MODIFIED_RESTAURANTS, mLastModifiedRestaurants);
        editor.putString(PREFERENCES_LAST_MODIFIED_INSPECTIONS, mLastModifiedInspections);
        editor.putLong(PREFERENCES_LAST_UPDATED, mLastUpdated);
        editor.apply();
    }
}

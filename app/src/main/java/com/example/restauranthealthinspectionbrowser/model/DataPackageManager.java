package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * DataPackageManager class stores the timestamps of data packages updates.
 * It uses SharedPreferences to manage data.
 */
public class DataPackageManager {
    private static final String TAG = "DataPackageManager";

    private static final String PREF = "dataPackages";
    private static final String PREF_LAST_UPDATED = "lastUpdated";
    private static final String PREF_LAST_MODIFIED_RESTAURANTS = "lastModifiedRestaurants";
    private static final String PREF_LAST_MODIFIED_INSPECTIONS = "lastModifiedInspections";

    private long mLastUpdated;
    private String mLastModifiedRestaurants;
    private String mLastModifiedInspections;
    private boolean mHasRequestedDownloadPermission;

    private static DataPackageManager sInstance;

    public static DataPackageManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataPackageManager(context);
        }
        return sInstance;
    }

    private DataPackageManager(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mLastUpdated = sp.getLong(PREF_LAST_UPDATED, 0);
        mLastModifiedRestaurants = sp.getString(PREF_LAST_MODIFIED_RESTAURANTS, "");
        mLastModifiedInspections = sp.getString(PREF_LAST_MODIFIED_INSPECTIONS, "");

        mHasRequestedDownloadPermission = false;
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

    public boolean isHasRequestedDownloadPermission() {
        return mHasRequestedDownloadPermission;
    }

    public void setHasRequestedDownloadPermission(boolean hasRequestedDownloadPermission) {
        mHasRequestedDownloadPermission = hasRequestedDownloadPermission;
    }

    public void updateLastModified(Context context, String lastModifiedRestaurants, String lastModifiedInspections) {
        mLastModifiedRestaurants = lastModifiedRestaurants;
        mLastModifiedInspections = lastModifiedInspections;
        mLastUpdated = System.currentTimeMillis();

        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREF_LAST_MODIFIED_RESTAURANTS, mLastModifiedRestaurants);
        editor.putString(PREF_LAST_MODIFIED_INSPECTIONS, mLastModifiedInspections);
        editor.putLong(PREF_LAST_UPDATED, mLastUpdated);
        editor.apply();
    }
}

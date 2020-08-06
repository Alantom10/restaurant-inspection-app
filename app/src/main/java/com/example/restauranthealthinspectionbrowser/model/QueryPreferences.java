package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.restauranthealthinspectionbrowser.databse.RestaurantDbSchema.RestaurantTable;

import java.util.ArrayList;
import java.util.List;

public class QueryPreferences {
    private static final String TAG = "QueryPreferences";
    private static final String PREF_TITLE_QUERY = "titleQuery";
    private static final String PREF_RATING_QUERY = "ratingQuery";

    public static String[] getStoredQuery(Context context) {
        List<String> query = new ArrayList<>();
        String clause = "";

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String titleQuery = sp.getString(PREF_TITLE_QUERY, null);
        if (titleQuery != null) {
            clause += RestaurantTable.Cols.TITLE + " LIKE ?";
            query.add(titleQuery);
        }

        String ratingQuery = sp.getString(PREF_RATING_QUERY, "Moderate");
        if (ratingQuery != null) {
            if (query.size() > 0) {
                clause += " AND ";
            }
            clause += RestaurantTable.Cols.RATING + " = ?";
            query.add(ratingQuery);
        }
        Log.i(TAG, "Query clause = " + clause);

        String[] result = new String[query.size() + 1];
        result[0] = clause;
        for (int i = 0; i < query.size(); i++) {
            result[i + 1] = query.get(i);
            Log.i(TAG, "Query argument = " + result[i]);
        }

        return result;
    }

    public static void setStoredTitleQuery(Context context, String query) {
        if (query == null) {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(PREF_TITLE_QUERY, null)
                    .apply();
        }
        else {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(PREF_TITLE_QUERY, "%" + query + "%")
                    .apply();
        }
    }
}

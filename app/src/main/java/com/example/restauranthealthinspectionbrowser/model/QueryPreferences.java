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

        String favouriteQuery = sp.getString(PREF_RATING_QUERY, null);
        if (favouriteQuery != null) {
            if (query.size() > 0) {
                clause += " AND ";
            }
            clause += RestaurantTable.Cols.FAVOURITE + " = ?";
            query.add(favouriteQuery);
        }

        String ratingQuery = sp.getString(PREF_RATING_QUERY, null);
        if (ratingQuery != null) {
            if (query.size() > 0) {
                clause += " AND ";
            }
            clause += RestaurantTable.Cols.RATING + " = ?";
            query.add(ratingQuery);
        }

        String maxIssuesQuery = sp.getString(PREF_RATING_QUERY, null);
        if (maxIssuesQuery != null) {
            if (query.size() > 0) {
                clause += " AND ";
            }
            clause += RestaurantTable.Cols.ISSUES + " < ?";
            query.add(maxIssuesQuery);
        }

        String minIssuesQuery = sp.getString(PREF_RATING_QUERY, null);
        if (minIssuesQuery != null) {
            if (query.size() > 0) {
                clause += " AND ";
            }
            clause += RestaurantTable.Cols.ISSUES + " > ?";
            query.add(minIssuesQuery);
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
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        if (query == null) {
            editor.putString(PREF_TITLE_QUERY, null)
                    .apply();
        }
        else {
            editor.putString(PREF_TITLE_QUERY, "%" + query + "%")
                    .apply();
        }
    }

    public static void setStoredFavouriteQuery(Context context, String query) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        if (query == null) {
            editor.putString(PREF_TITLE_QUERY, null)
                    .apply();
        }
        else {
            editor.putString(PREF_TITLE_QUERY, query)
                    .apply();
        }
    }

    public static void setStoredRatingQuery(Context context, String query) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        if (query == null) {
            editor.putString(PREF_TITLE_QUERY, null)
                    .apply();
        }
        else {
            editor.putString(PREF_TITLE_QUERY, query)
                    .apply();
        }
    }

    public static void setStoredMaximumIssuesQuery(Context context, String query) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        if (query == null) {
            editor.putString(PREF_TITLE_QUERY, null)
                    .apply();
        }
        else {
            editor.putString(PREF_TITLE_QUERY, query)
                    .apply();
        }
    }

    public static void setStoredMinimumIssuesQuery(Context context, String query) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        if (query == null) {
            editor.putString(PREF_TITLE_QUERY, null)
                    .apply();
        }
        else {
            editor.putString(PREF_TITLE_QUERY, query)
                    .apply();
        }
    }
}

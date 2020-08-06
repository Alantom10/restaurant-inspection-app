package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.restauranthealthinspectionbrowser.databse.RestaurantDbSchema.RestaurantTable;

import java.util.ArrayList;
import java.util.List;

/**
 * QueryPreferences sets and stores queries from search and filter.
 */
public class QueryPreferences {
    private static final String TAG = "QueryPreferences";
    private static final String PREF_QUERY_TITLE = "titleQuery";
    private static final String PREF_QUERY_FAVOURITE = "favouriteQuery";
    private static final String PREF_QUERY_RATING = "ratingQuery";
    private static final String PREF_QUERY_MAX_ISSUES = "maxIssuesQuery";
    private static final String PREF_QUERY_MIN_ISSUES = "minIssuesQuery";

    public static String[] getStoredQuery(Context context) {
        List<String> query = new ArrayList<>();
        String clause = "";

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String titleQuery = sp.getString(PREF_QUERY_TITLE, null);
        if (titleQuery != null) {
            clause += RestaurantTable.Cols.TITLE + " LIKE ?";
            query.add(titleQuery);
        }

        String favouriteQuery = sp.getString(PREF_QUERY_FAVOURITE, null);
        if (favouriteQuery != null) {
            if (query.size() > 0) {
                clause += " AND ";
            }
            clause += RestaurantTable.Cols.FAVOURITE + " = ?";
            query.add(favouriteQuery);
        }

        String ratingQuery = sp.getString(PREF_QUERY_RATING, null);
        if (ratingQuery != null) {
            if (query.size() > 0) {
                clause += " AND ";
            }
            clause += RestaurantTable.Cols.RATING + " = ?";
            query.add(ratingQuery);
        }

        String maxIssuesQuery = sp.getString(PREF_QUERY_MAX_ISSUES, null);
        if (maxIssuesQuery != null) {
            if (query.size() > 0) {
                clause += " AND ";
            }
            clause += RestaurantTable.Cols.ISSUES + " <= ?";
            query.add(maxIssuesQuery);
        }

        String minIssuesQuery = sp.getString(PREF_QUERY_MIN_ISSUES, null);
        if (minIssuesQuery != null) {
            if (query.size() > 0) {
                clause += " AND ";
            }
            clause += RestaurantTable.Cols.ISSUES + " >= ?";
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
            editor.putString(PREF_QUERY_TITLE, null)
                    .apply();
        }
        else {
            editor.putString(PREF_QUERY_TITLE, "%" + query + "%")
                    .apply();
        }
    }

    public static void setStoredFavouriteQuery(Context context, Boolean isFavourite) {
        String query = null;

        if (isFavourite) {
            query = "1";
        }

        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        if (query == null) {
            editor.putString(PREF_QUERY_FAVOURITE, null)
                    .apply();
        }
        else {
            editor.putString(PREF_QUERY_FAVOURITE, query)
                    .apply();
        }
    }

    public static void setStoredRatingQuery(Context context, String query) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        if (query == null) {
            editor.putString(PREF_QUERY_RATING, null)
                    .apply();
        }
        else {
            editor.putString(PREF_QUERY_RATING, query)
                    .apply();
        }
    }

    public static void setStoredMaximumIssuesQuery(Context context, String query) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        if (query == null) {
            editor.putString(PREF_QUERY_MAX_ISSUES, null)
                    .apply();
        }
        else {
            editor.putString(PREF_QUERY_MAX_ISSUES, query)
                    .apply();
        }
    }

    public static void setStoredMinimumIssuesQuery(Context context, String query) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        if (query == null) {
            editor.putString(PREF_QUERY_MIN_ISSUES, null)
                    .apply();
        }
        else {
            editor.putString(PREF_QUERY_MIN_ISSUES, query)
                    .apply();
        }
    }
}

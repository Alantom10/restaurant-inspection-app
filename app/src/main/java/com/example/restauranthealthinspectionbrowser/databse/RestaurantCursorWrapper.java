package com.example.restauranthealthinspectionbrowser.databse;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.example.restauranthealthinspectionbrowser.databse.RestaurantDbSchema.RestaurantTable;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;

import java.util.Date;

/**
 * Wrapper class for Cursor that delegates calls to the cursor object. It
 * returns a restaurant object.
 */
public class RestaurantCursorWrapper extends CursorWrapper {
    private static final String TAG = "RestaurantCursorWrapper";
    public RestaurantCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Restaurant getRestaurant() {
        String id = getString(getColumnIndex(RestaurantTable.Cols.ID));
        String title = getString(getColumnIndex(RestaurantTable.Cols.TITLE));
        String address = getString(getColumnIndex(RestaurantTable.Cols.ADDRESS));
        Double latitude = Double.parseDouble(getString(getColumnIndex(RestaurantTable.Cols.LATITUDE)));
        Double longitude = Double.parseDouble(getString(getColumnIndex(RestaurantTable.Cols.LONGITUDE)));
        String issues = getString(getColumnIndex(RestaurantTable.Cols.ISSUES));
        String rating = getString(getColumnIndex(RestaurantTable.Cols.RATING));
        Date date = new Date(getLong(getColumnIndex(RestaurantTable.Cols.DATE)));
        String isFavourite = getString(getColumnIndex(RestaurantTable.Cols.FAVOURITE));
        String isUpdated = getString(getColumnIndex(RestaurantTable.Cols.UPDATED));
//        Log.i(TAG, "Favourite: " + isFavourite);

        Restaurant restaurant = new Restaurant(id);
        restaurant.setTitle(title);
        restaurant.setAddress(address);
        restaurant.setLatitude(latitude);
        restaurant.setLongitude(longitude);
        restaurant.setIssues(Integer.parseInt(issues));
        restaurant.setRating(rating);
        restaurant.setDate(date);
        restaurant.setFavourite(isFavourite.equals("1"));
        restaurant.setUpdated(isUpdated.equals("1"));

        return restaurant;
    }
}
package com.example.restauranthealthinspectionbrowser.databse;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.restauranthealthinspectionbrowser.databse.RestaurantDbSchema.RestaurantTable;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;

import java.util.Date;

public class RestaurantCursorWrapper extends CursorWrapper {
    public RestaurantCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Restaurant getRestaurant() {
        String id = getString(getColumnIndex(RestaurantTable.Cols.ID));
        String title = getString(getColumnIndex(RestaurantTable.Cols.TITLE));
        String address = getString(getColumnIndex(RestaurantTable.Cols.ADDRESS));
        Double latitude = getDouble(getColumnIndex(RestaurantTable.Cols.LATITUDE));
        Double longitude = getDouble(getColumnIndex(RestaurantTable.Cols.LONGITUDE));
        int issues = getInt(getColumnIndex(RestaurantTable.Cols.ISSUES));
        String rating = getString(getColumnIndex(RestaurantTable.Cols.RATING));
        Date date = new Date(getLong(getColumnIndex(RestaurantTable.Cols.DATE)));

        Restaurant restaurant = new Restaurant(id);
        restaurant.setTitle(title);
        restaurant.setAddress(address);
        restaurant.setLatitude(latitude);
        restaurant.setLongitude(longitude);
        restaurant.setIssues(issues);
        restaurant.setRating(rating);
        restaurant.setDate(date);

        return restaurant;
    }
}

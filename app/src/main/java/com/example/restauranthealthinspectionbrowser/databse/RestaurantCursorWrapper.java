package com.example.restauranthealthinspectionbrowser.databse;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.restauranthealthinspectionbrowser.databse.RestaurantDbSchema.RestaurantTable;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;

public class RestaurantCursorWrapper extends CursorWrapper {
    public RestaurantCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Restaurant getRestaurant() {
        String id = getString(getColumnIndex(RestaurantTable.Cols.ID));
        String title = getString(getColumnIndex(RestaurantTable.Cols.TITLE));
        long date = getLong(getColumnIndex(RestaurantTable.Cols.DATE));
        int isFavourite = getInt(getColumnIndex(RestaurantTable.Cols.FAVOURITE));

        Restaurant restaurant = new Restaurant(id);
        restaurant.setTitle(title);

        return restaurant;
    }
}

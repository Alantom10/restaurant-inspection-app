package com.example.restauranthealthinspectionbrowser.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.restauranthealthinspectionbrowser.R;
import com.example.restauranthealthinspectionbrowser.databse.RestaurantBaseHelper;
import com.example.restauranthealthinspectionbrowser.databse.RestaurantCursorWrapper;
import com.example.restauranthealthinspectionbrowser.databse.RestaurantDbSchema.RestaurantTable;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.restauranthealthinspectionbrowser.ui.MapsActivity.FILE_NAME_RESTAURANTS;

/**
 * RestaurantManager class stores a collection of restaurants. It supports
 * reading restaurant date from file.
 */
public class RestaurantManager {
    private static final String TAG = "RestaurantManager";

    private List<Restaurant> mRestaurants;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private RestaurantManager(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new RestaurantBaseHelper(mContext).getWritableDatabase();

        mRestaurants = new ArrayList<>();
        writeDataFileToDatabase(context);
        Collections.sort(mRestaurants);
    }

    public Restaurant getRestaurant(String id) {
        RestaurantCursorWrapper cursor = queryRestaurants(
                RestaurantTable.Cols.ID + " = ?",
                new String[] { id }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getRestaurant();
        } finally {
            cursor.close();
        }
    }

    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();

        RestaurantCursorWrapper cursor = queryRestaurants(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                restaurants.add(cursor.getRestaurant());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return restaurants;
    }

    private RestaurantCursorWrapper queryRestaurants(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RestaurantTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new RestaurantCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Restaurant restaurant) {
        ContentValues values = new ContentValues();
        values.put(RestaurantTable.Cols.ID, restaurant.getId());
        values.put(RestaurantTable.Cols.TITLE, restaurant.getTitle());
        return values;
    }

    public void addRestaurant(Restaurant restaurant) {
        ContentValues values = getContentValues(restaurant);
        mDatabase.insert(RestaurantTable.NAME, null, values);
    }

    public void updateRestaurant(Restaurant restaurant) {
        String id = restaurant.getId();
        ContentValues values = getContentValues(restaurant);
        mDatabase.update(RestaurantTable.NAME, values,
                RestaurantTable.Cols.ID + " = ?",
                new String[] { id });
    }

    public void updateRestaurantDatabase(Context context) throws FileNotFoundException {
        mRestaurants.clear();
        writeDataFileToDatabase(context);
        Collections.sort(mRestaurants);
    }

    private void writeDataFileToDatabase(Context context)  {
        // Adapted from https://www.youtube.com/watch?v=i-TqNzUryn8
        File file = new File(context.getFilesDir() + "/" + FILE_NAME_RESTAURANTS);
        InputStream inputStream = null;
        if (file.exists()) {
            try {
                inputStream = context.openFileInput(FILE_NAME_RESTAURANTS);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            inputStream = context.getResources().openRawResource(R.raw.restaurants_itr1);
        }

        if(inputStream == null){
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        try {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length > 7) {
                    int shift = row.length - 7;
                    while (shift > 0) {
                        row[1] += ", " + row[2];
                        for (int i = 2; i < shift + 6; i++) {
                            row[i] = row[i + 1];
                        }
                        shift--;
                    }
                }

                Restaurant restaurant = new Restaurant();
                restaurant.setId(row[0].replace("\"", ""));
                restaurant.setTitle(row[1].replace("\"", ""));
                restaurant.setAddress((row[2] + ", " + row[3]).replace("\"", ""));
                restaurant.setLatitude(Double.parseDouble(row[5]));
                restaurant.setLongitude(Double.parseDouble(row[6]));
                mRestaurants.add(restaurant);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Restaurant getRestaurant(LatLng latLng) {
        for (Restaurant mRestaurant : mRestaurants) {
            if (mRestaurant.getLatitude() == latLng.latitude && mRestaurant.getLongitude() == latLng.longitude){
                return mRestaurant;
            }
        }
        return null;
    }
}

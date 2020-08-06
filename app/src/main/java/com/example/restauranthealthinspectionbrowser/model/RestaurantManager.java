package com.example.restauranthealthinspectionbrowser.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
import java.util.List;

import static com.example.restauranthealthinspectionbrowser.ui.MapsActivity.FILE_NAME_RESTAURANTS;

/**
 * RestaurantManager class stores a collection of restaurants. It supports
 * reading restaurant date from file.
 */
public class RestaurantManager {
    private static final String TAG = "RestaurantManager";

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public RestaurantManager(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new RestaurantBaseHelper(mContext).getWritableDatabase();

        long lastUpdated = DataPackageManager.getInstance(context)
                .getLastUpdated();
        if (lastUpdated == 0) {
            readDataFileIntoDatabase(context);
        }
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

    public Restaurant getRestaurant(LatLng latLng) {
        RestaurantCursorWrapper cursor = queryRestaurants(
                RestaurantTable.Cols.LATITUDE + " = ? AND " +
                        RestaurantTable.Cols.LONGITUDE + " = ?",
                new String[] { Double.toString(latLng.latitude),
                        Double.toString(latLng.longitude) }
        );
        Log.i(TAG, "Lat = " + latLng.latitude + "; Lon = " + latLng.longitude);

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

    public void updateRestaurant(Restaurant restaurant) {
        ContentValues values = getContentValues(restaurant);
        mDatabase.update(RestaurantTable.NAME, values,
                RestaurantTable.Cols.ID + " = ?",
                new String[] { restaurant.getId() });
    }

    private static ContentValues getContentValues(Restaurant restaurant) {
        ContentValues values = new ContentValues();
        values.put(RestaurantTable.Cols.ID, restaurant.getId());
        values.put(RestaurantTable.Cols.TITLE, restaurant.getTitle());
        values.put(RestaurantTable.Cols.ADDRESS, restaurant.getAddress());
        values.put(RestaurantTable.Cols.LATITUDE, Double.toString(restaurant.getLatitude()));
        values.put(RestaurantTable.Cols.LONGITUDE, Double.toString(restaurant.getLatitude()));
        values.put(RestaurantTable.Cols.ISSUES, restaurant.getIssues());
        values.put(RestaurantTable.Cols.RATING, restaurant.getRating());
        values.put(RestaurantTable.Cols.DATE, restaurant.getDate().getTime());
        values.put(RestaurantTable.Cols.FAVOURITE, restaurant.isFavourite() ? 1 : 0);

        return values;
    }

    public List<Restaurant> getRestaurants() {
        RestaurantCursorWrapper cursor = queryRestaurants(null, null);
        return iterateRestaurantCursor(cursor);
    }

    public List<Restaurant> getRestaurants(String query) {
        if (query == null) {
            return getRestaurants();
        }
        else {
            RestaurantCursorWrapper cursor = queryRestaurants(
                    RestaurantTable.Cols.TITLE + " LIKE ?",
                    new String[] { "%" + query + "%" }
            );

            return iterateRestaurantCursor(cursor);
        }
    }

    public List<Restaurant> iterateRestaurantCursor(RestaurantCursorWrapper cursor) {
        List<Restaurant> restaurants = new ArrayList<>();

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                restaurants.add(cursor.getRestaurant());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Collections.sort(restaurants);

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

    public void updateRestaurantDatabase(Context context) throws FileNotFoundException {
        readDataFileIntoDatabase(context);
    }

    private void readDataFileIntoDatabase(Context context)  {
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

                String id = row[0].replace("\"", "");
                String title = row[1].replace("\"", "");
                String address = (row[2] + ", " + row[3]).replace("\"", "");
                String latitude = row[5];
                String longitude = row[6];
                int issues = 0;
                String rating = "";
                long date = 0;

                Inspection inspection = InspectionManager.getInstance(context)
                        .getLatestInspection(id);
                if (inspection != null) {
                    issues = inspection.getNumCritical() + inspection.getNumNonCritical();
                    rating = inspection.getHazardRating();
                    date = inspection.getInspectionDate().getTime();
                }

                ContentValues values = new ContentValues();
                values.put(RestaurantTable.Cols.ID, id);
                values.put(RestaurantTable.Cols.TITLE, title);
                values.put(RestaurantTable.Cols.ADDRESS, address);
                values.put(RestaurantTable.Cols.LATITUDE, latitude);
                values.put(RestaurantTable.Cols.LONGITUDE, longitude);
                values.put(RestaurantTable.Cols.ISSUES, issues);
                values.put(RestaurantTable.Cols.RATING, rating);
                values.put(RestaurantTable.Cols.DATE, date);

                Restaurant restaurant = getRestaurant(id);
                if (restaurant == null) {
                    mDatabase.insert(RestaurantTable.NAME, null, values);
                }
                else {
                    mDatabase.update(RestaurantTable.NAME, values,
                            RestaurantTable.Cols.ID + " = ?",
                            new String[] { id });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

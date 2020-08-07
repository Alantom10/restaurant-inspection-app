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
import java.util.Date;
import java.util.List;
import java.util.Random;

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
//        Log.i(TAG, "Lat = " + latLng.latitude + "; Lon = " + latLng.longitude);

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
        values.put(RestaurantTable.Cols.LONGITUDE, Double.toString(restaurant.getLongitude()));
        values.put(RestaurantTable.Cols.ISSUES, restaurant.getIssues());
        values.put(RestaurantTable.Cols.RATING, restaurant.getRating());
        values.put(RestaurantTable.Cols.DATE, restaurant.getDate().getTime());
        values.put(RestaurantTable.Cols.FAVOURITE, restaurant.isFavourite() ? 1 : 0);
        values.put(RestaurantTable.Cols.UPDATED, restaurant.isUpdated() ? 1 : 0);
        values.put(RestaurantTable.Cols.CRITICAL, restaurant.getCriticalIssues());

        return values;
    }

    public List<Restaurant> getRestaurants() {
        RestaurantCursorWrapper cursor = queryRestaurants(null, null);
        return iterateRestaurantCursor(cursor);
    }

    public List<Restaurant> getRestaurants(String[] query) {
        String clause = query[0];
        Log.i(TAG, "Query clause: " + clause);

        if (clause.equals("")) {
            return getRestaurants();
        }
        else {
            String[] args = new String[query.length - 1];

            for (int i = 0; i < args.length; i++) {
                args[i] = query[i + 1];
                Log.i(TAG, "Query argument: " + args[i]);
            }

            RestaurantCursorWrapper cursor = queryRestaurants(
                    clause,
                    args
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

                Random rand = new Random();
                Double latitude = Double.parseDouble(row[5]);
                Double longitude = Double.parseDouble(row[6]);
                latitude += rand.nextInt(10) * Math.pow(10, -4);
                longitude += rand.nextInt(10) * Math.pow(10, -4);

                int issues = 0;
                String rating = "";
                Date date = new Date(0);
//                Log.i(TAG, "Date/time: " + date.getTime());

                InspectionManager im = InspectionManager.getInstance(context);

                int criticalIssues = im.getCriticalIssues(id);

                Inspection inspection = im.getLatestInspection(id);
                if (inspection != null) {
                    issues = inspection.getNumCritical() + inspection.getNumNonCritical();
                    rating = inspection.getHazardRating();
                    date = inspection.getInspectionDate();
                }

                Restaurant restaurant = getRestaurant(id);
                if (restaurant == null) {
                    restaurant = new Restaurant(id);
                    restaurant.setTitle(title);
                    restaurant.setAddress(address);
                    restaurant.setLatitude(latitude);
                    restaurant.setLongitude(longitude);
                    restaurant.setIssues(issues);
                    restaurant.setRating(rating);
                    restaurant.setDate(date);
                    restaurant.setFavourite(false);
                    restaurant.setUpdated(false);
                    restaurant.setCriticalIssues(criticalIssues);

                    ContentValues values = getContentValues(restaurant);
                    mDatabase.insert(RestaurantTable.NAME, null, values);
                }
                else {
                    long time = restaurant.getDate().getTime();
                    Log.i(TAG, "New date: " + date.getTime() + "; Old date: " + time);
                    if (time < date.getTime()) {
                        restaurant.setUpdated(true);
                    }
                    else {
                        restaurant.setUpdated(false);
                    }

                    restaurant.setIssues(issues);
                    restaurant.setRating(rating);
                    restaurant.setDate(date);
                    restaurant.setCriticalIssues(criticalIssues);

                    ContentValues values = getContentValues(restaurant);
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

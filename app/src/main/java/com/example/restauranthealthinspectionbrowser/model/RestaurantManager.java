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

    private static RestaurantManager sInstance;

    public static RestaurantManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RestaurantManager(context);
        }
        return sInstance;
    }

    private RestaurantManager(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new RestaurantBaseHelper(mContext).getWritableDatabase();
        mRestaurants = new ArrayList<>();
        readData(context);
        Collections.sort(mRestaurants);
    }

    public Restaurant getRestaurant(String id) {
        for (Restaurant restaurant : mRestaurants) {
            if (restaurant.getId().equals(id)) {
                return restaurant;
            }
        }
        return null;
    }

    public void updateRestaurant(Restaurant restaurant) {
        String id = restaurant.getId();
        ContentValues values = getContentValues(restaurant);
        mDatabase.update(RestaurantTable.NAME, values,
                RestaurantTable.Cols.ID + " = ?",
                new String[] { id });
    }

    private RestaurantCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
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

    public List<Restaurant> getRestaurantList() {
        return mRestaurants;
    }

    public void updateRestaurantList(Context context) throws FileNotFoundException {
        mRestaurants.clear();
        readData(context);
        Collections.sort(mRestaurants);
    }

    private void readData(Context context)  {
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

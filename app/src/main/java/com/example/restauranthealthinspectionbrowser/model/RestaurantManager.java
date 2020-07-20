package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;

import com.example.restauranthealthinspectionbrowser.R;

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

    private static RestaurantManager sInstance;

    public static RestaurantManager getInstance(Context context) throws FileNotFoundException {
        if (sInstance == null) {
            sInstance = new RestaurantManager(context);
        }
        return sInstance;
    }

    private RestaurantManager(Context context) throws FileNotFoundException {
        mRestaurants = new ArrayList<>();
        readData(context);
        Collections.sort(mRestaurants);
    }

    public Restaurant getRestaurant(String id) {
        for (Restaurant restaurant : mRestaurants) {
            if (restaurant.getID().equals(id)) {
                return restaurant;
            }
        }
        return null;
    }

    public List<Restaurant> getRestaurants() {
        return mRestaurants;
    }

    public void updateRestaurants(Context context) throws FileNotFoundException {
        mRestaurants.clear();
        readData(context);
        Collections.sort(mRestaurants);
    }

    private void readData(Context context) throws FileNotFoundException {
        // Adapted from https://www.youtube.com/watch?v=i-TqNzUryn8
        File file = new File(context.getFilesDir() + "/" + FILE_NAME_RESTAURANTS);
        InputStream inputStream;
        if (file.exists()) {
            inputStream = context.openFileInput(FILE_NAME_RESTAURANTS);
        }
        else {
            inputStream = context.getResources().openRawResource(R.raw.restaurants_itr1);
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
                restaurant.setID(row[0].replace("\"", ""));
                restaurant.setName(row[1].replace("\"", ""));
                restaurant.setAddress((row[2] + ", " + row[3]).replace("\"", ""));
                restaurant.setLatitude(Double.parseDouble(row[5]));
                restaurant.setLongitude(Double.parseDouble(row[6]));
                mRestaurants.add(restaurant);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

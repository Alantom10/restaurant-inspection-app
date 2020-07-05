package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;

import com.example.restauranthealthinspectionbrowser.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantManager {
    private static RestaurantManager sInstance;

    private List<Restaurant> mRestaurants;

    public static RestaurantManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RestaurantManager(context);
        }
        return sInstance;
    }

    private RestaurantManager(Context context) {
        mRestaurants = new ArrayList<>();
        readRestaurantData(context);
        Collections.sort(mRestaurants);
    }

    public List<Restaurant> getRestaurants() {
        return mRestaurants;
    }

    public Restaurant getRestaurant(String id) {
        for (Restaurant restaurant : mRestaurants) {
            if (restaurant.getID().equals(id)) {
                return restaurant;
            }
        }
        return null;
    }

    private void readRestaurantData(Context context) {
        // Adapted from https://www.youtube.com/watch?v=i-TqNzUryn8&feature=youtu.be
        InputStream is = context.getResources().openRawResource(R.raw.restaurants_itr1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        try {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                Restaurant restaurant = new Restaurant();
                restaurant.setID(row[0].replace("\"", ""));
                restaurant.setName(row[1].replace("\"", ""));
                restaurant.setAddress(row[2].replace("\"", ""));
                restaurant.setLatitude(Double.parseDouble(row[5]));
                restaurant.setLongitude(Double.parseDouble(row[6]));
                mRestaurants.add(restaurant);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

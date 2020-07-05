package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;
import android.util.Log;

import com.example.restauranthealthinspectionbrowser.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
        InputStream is = context.getResources().openRawResource(R.raw.restaurants_itr1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        try {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                Restaurant restaurant = new Restaurant();
                restaurant.setID(row[0]);
                restaurant.setName(row[1]);
                restaurant.setAddress(row[2]);
                restaurant.setLatitude(Double.parseDouble(row[5]));
                restaurant.setLongitude(Double.parseDouble(row[6]));
                mRestaurants.add(restaurant);

                Log.d("tag", row[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

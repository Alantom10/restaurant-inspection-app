package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;

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
    }

    public List<Restaurant> getRestaurants() {
        return mRestaurants;
    }
}

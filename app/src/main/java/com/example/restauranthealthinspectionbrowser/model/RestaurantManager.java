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
        Restaurant restaurant = new Restaurant();
        restaurant.setID("SDFO-8HKP7E");
        restaurant.setName("Pattullo A&W");
        restaurant.setAddress("12808 King George Blvd, Surrey");
        mRestaurants.add(restaurant);
        restaurant.setID("SHEN-B7BNSR");
        restaurant.setName("Lee Yuen Seafood Restaurant");
        restaurant.setAddress("1812 152 St, Surrey");
        mRestaurants.add(restaurant);
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
}

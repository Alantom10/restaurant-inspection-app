package com.example.restauranthealthinspectionbrowser.ui;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class RestaurantActivity extends SingleFragmentActivity {

    public static final String EXTRA_RESTAURANT_ID = "restaurant id";

    public static Intent makeIntent(Context context, String restaurantID) {
        Intent intent = new Intent(context, RestaurantActivity.class);
        intent.putExtra(EXTRA_RESTAURANT_ID, restaurantID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new RestaurantFragment();
    }
}

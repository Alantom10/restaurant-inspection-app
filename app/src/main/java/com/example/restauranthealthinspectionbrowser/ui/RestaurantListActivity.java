package com.example.restauranthealthinspectionbrowser.ui;

import androidx.fragment.app.Fragment;

import static com.example.restauranthealthinspectionbrowser.ui.MapsActivity.main;

/**
 * MainActivity initializes the main screen of the app through RestaurantListFragment.
 * The main screen shows a list of restaurants.
 */
public class RestaurantListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RestaurantListFragment();
    }

    @Override
    public void onBackPressed() {
        main.finish();
        finish();
    }
}

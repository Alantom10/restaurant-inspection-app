package com.example.restauranthealthinspectionbrowser.ui;

import androidx.fragment.app.Fragment;

/**
 * MainActivity initializes the main screen of the app through RestaurantListFragment.
 * The main screen shows a list of restaurants.
 */
public class RestaurantListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RestaurantListFragment();
    }
}

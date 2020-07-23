package com.example.restauranthealthinspectionbrowser.ui;

import androidx.fragment.app.Fragment;

import static com.example.restauranthealthinspectionbrowser.ui.MapsActivity.main;

/**
 * RestaurantListActivity initializes the restaurant list view screen through
 * RestaurantListFragment.
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

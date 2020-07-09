package com.example.restauranthealthinspectionbrowser.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.restauranthealthinspectionbrowser.R;

/**
 * RestaurantActivity initializes the Inspections screen through RestaurantFragment.
 * The Inspections screen shows info on the restaurant and a list of inspections.
 */
public class RestaurantActivity extends SingleFragmentActivity {

    public static final String EXTRA_RESTAURANT_ID = "restaurant id";

    public static Intent makeIntent(Context context, String restaurantID) {
        Intent intent = new Intent(context, RestaurantActivity.class);
        intent.putExtra(EXTRA_RESTAURANT_ID, restaurantID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(getString(R.string.restaurant_activity_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Fragment createFragment() {
        return new RestaurantFragment();
    }
}

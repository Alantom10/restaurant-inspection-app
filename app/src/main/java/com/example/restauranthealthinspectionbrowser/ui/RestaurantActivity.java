package com.example.restauranthealthinspectionbrowser.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranthealthinspectionbrowser.R;

public class RestaurantActivity extends AppCompatActivity {

    public static final String EXTRA_RESTAURANT_ID = "restaurant id";

    public static Intent makeIntent(Context context, String restaurantID) {
        Intent intent = new Intent(context, RestaurantActivity.class);
        intent.putExtra(EXTRA_RESTAURANT_ID, restaurantID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
    }
}

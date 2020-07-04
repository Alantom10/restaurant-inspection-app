package com.example.restauranthealthinspectionbrowser.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.restauranthealthinspectionbrowser.R;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RestaurantListFragment();
    }
}

package com.example.restauranthealthinspectionbrowser.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.restauranthealthinspectionbrowser.R;
import com.example.restauranthealthinspectionbrowser.adapter.ViolationAdapter;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;
import com.example.restauranthealthinspectionbrowser.model.RestaurantManager;

import static com.example.restauranthealthinspectionbrowser.ui.RestaurantActivity.EXTRA_RESTAURANT_ID;

public class RestaurantFragment extends Fragment {
    private TextView mTitleTextView;
    private TextView mAddressTextView;
    private TextView mCoordinatesTextView;

    private Restaurant mRestaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        String restaurantID = getActivity().getIntent().getStringExtra(EXTRA_RESTAURANT_ID);
        mRestaurant = RestaurantManager.getInstance(getActivity()).getRestaurant(restaurantID);

        updateUI(view);

//        Button button = (Button) view.findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), InspectionActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }

    private void updateUI(View view) {
        mTitleTextView = (TextView) view.findViewById(R.id.title);
        mTitleTextView.setText(getString(R.string.restaurant_name, mRestaurant.getName()));

        mAddressTextView = (TextView) view.findViewById(R.id.address);
        mAddressTextView.setText(getString(R.string.address, mRestaurant.getAddress()));

        mCoordinatesTextView = (TextView) view.findViewById(R.id.coordinates);
        mCoordinatesTextView.setText(getString(R.string.coordinates, mRestaurant.getLatitude(), mRestaurant.getLongitude()));
    }

    private void dateToString(){

    }
}

package com.example.restauranthealthinspectionbrowser.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthealthinspectionbrowser.R;
import com.example.restauranthealthinspectionbrowser.model.DateHelper;
import com.example.restauranthealthinspectionbrowser.model.Inspection;
import com.example.restauranthealthinspectionbrowser.model.InspectionManager;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;
import com.example.restauranthealthinspectionbrowser.model.RestaurantManager;

import java.util.List;

public class RestaurantListFragment extends Fragment {
    private RecyclerView mRestaurantRecyclerView;
    private RestaurantAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        mRestaurantRecyclerView = (RecyclerView) view.findViewById(R.id.restaurant_recycler_view);
        mRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        RestaurantManager manager = RestaurantManager.getInstance(getActivity());
        List<Restaurant> restaurants = manager.getRestaurants();

        mAdapter = new RestaurantAdapter(restaurants);
        mRestaurantRecyclerView.setAdapter(mAdapter);
    }

    private class RestaurantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mNumIssuesTextView;
        private TextView mHazardLevelTextView;
        private ImageView mHazardLevelImageView;

        private String mRestaurantID;

        public RestaurantHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_restaurant, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.title);
            mDateTextView = (TextView) itemView.findViewById(R.id.inspection_date);
            mNumIssuesTextView = (TextView) itemView.findViewById(R.id.num_issues);
            mHazardLevelTextView = (TextView) itemView.findViewById(R.id.hazard_level);
            mHazardLevelImageView = (ImageView) itemView.findViewById(R.id.hazard_level_icon);
        }

        public void bind(Restaurant restaurant) {
            mRestaurantID = restaurant.getID();
            InspectionManager manager = InspectionManager.getInstance(getActivity());
            Inspection inspection = manager.getLatestInspection(mRestaurantID);

            mTitleTextView.setText(getString(R.string.restaurant_name, restaurant.getName()));
            mDateTextView.setText(DateHelper.getDisplayDate(inspection.getInspectionDate()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = RestaurantActivity.makeIntent(getActivity(), mRestaurantID);
            startActivity(intent);
        }
    }

    private class RestaurantAdapter extends RecyclerView.Adapter<RestaurantHolder> {
        private List<Restaurant> mRestaurants;

        public RestaurantAdapter(List<Restaurant> restaurants) {
            mRestaurants = restaurants;
        }

        @NonNull
        @Override
        public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new RestaurantHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RestaurantHolder holder, int position) {
            Restaurant restaurant = mRestaurants.get(position);
            holder.bind(restaurant);
        }

        @Override
        public int getItemCount() {
            return mRestaurants.size();
        }
    }
}

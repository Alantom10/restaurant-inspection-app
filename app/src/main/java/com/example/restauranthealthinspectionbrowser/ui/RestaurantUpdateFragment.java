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
import com.example.restauranthealthinspectionbrowser.model.HazardRatingHelper;
import com.example.restauranthealthinspectionbrowser.model.Inspection;
import com.example.restauranthealthinspectionbrowser.model.InspectionManager;
import com.example.restauranthealthinspectionbrowser.model.QueryPreferences;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;
import com.example.restauranthealthinspectionbrowser.model.RestaurantIconHelper;
import com.example.restauranthealthinspectionbrowser.model.RestaurantManager;

import java.util.List;

public class RestaurantUpdateFragment extends Fragment {
    private RecyclerView mRestaurantRecyclerView;
    private RestaurantUpdateFragment.RestaurantAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_update, container, false);

        mRestaurantRecyclerView = (RecyclerView) view.findViewById(R.id.restaurant_recycler_view);
        mRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        String[] query = QueryPreferences.getStoredQuery(getActivity());
        List<Restaurant> restaurants = new RestaurantManager(getActivity())
                .getRestaurants(query);

        if (mAdapter == null) {
            mAdapter = new RestaurantUpdateFragment.RestaurantAdapter(restaurants);
            mRestaurantRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setRestaurants(restaurants);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class RestaurantHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private ImageView mRestaurantIcon;
        private TextView mDateTextView;
        private TextView mNumIssuesTextView;
        private TextView mHazardLevelTextView;
        private ImageView mHazardLevelImageView;
        private ImageView mFavouriteImageView;

        private String mRestaurantID;

        public RestaurantHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_restaurant, parent, false));

            mTitleTextView = (TextView) itemView.findViewById(R.id.title);
            mRestaurantIcon = (ImageView) itemView.findViewById(R.id.restaurant_icon);
            mDateTextView = (TextView) itemView.findViewById(R.id.inspection_date);
            mNumIssuesTextView = (TextView) itemView.findViewById(R.id.num_issues);
            mHazardLevelTextView = (TextView) itemView.findViewById(R.id.hazard_level);
            mHazardLevelImageView = (ImageView) itemView.findViewById(R.id.hazard_level_icon);
            mFavouriteImageView = (ImageView) itemView.findViewById(R.id.favourite_icon);
        }

        public void bind(Restaurant restaurant) {
            mRestaurantID = restaurant.getId();
            mTitleTextView.setText(getString(R.string.restaurant_name, restaurant.getTitle()));

            Inspection inspection = InspectionManager.getInstance(getActivity())
                    .getLatestInspection(mRestaurantID);
            if (inspection != null) {
                mDateTextView.setText(DateHelper.getDisplayDate(inspection.getInspectionDate()));

                int numIssues = inspection.getNumCritical() + inspection.getNumNonCritical();
                mNumIssuesTextView.setText(getString(R.string.num_issues, numIssues));

                String hazardLevel = inspection.getHazardRating();
                mHazardLevelTextView.setText(getString(R.string.hazard_level, hazardLevel));

                HazardRatingHelper helper = new HazardRatingHelper();
                mHazardLevelTextView.setTextColor(
                        ContextCompat.getColor(getActivity(),
                                helper.getHazardColor(hazardLevel))
                );
                mHazardLevelImageView.setImageResource(helper.getHazardIcon(hazardLevel));
            }
            else {
                mDateTextView.setText("");
                mNumIssuesTextView.setText(R.string.no_inspection_info);
                mHazardLevelTextView.setText("");
                mHazardLevelImageView.setImageResource(R.drawable.blank_icon);
            }

            int iconResId = new RestaurantIconHelper().getIconResId(restaurant.getTitle());
            mRestaurantIcon.setImageResource(iconResId);

            if (restaurant.isFavourite()) {
                mFavouriteImageView.setImageResource(R.drawable.ic_baseline_star_24);
            }
            else {
                mFavouriteImageView.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
        }
    }

    private class RestaurantAdapter extends RecyclerView.Adapter<RestaurantUpdateFragment.RestaurantHolder> {
        private List<Restaurant> mRestaurants;

        public RestaurantAdapter(List<Restaurant> restaurants) {
            mRestaurants = restaurants;
        }

        @NonNull
        @Override
        public RestaurantUpdateFragment.RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new RestaurantUpdateFragment.RestaurantHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RestaurantUpdateFragment.RestaurantHolder holder, int position) {
            Restaurant restaurant = mRestaurants.get(position);
            holder.bind(restaurant);
        }

        @Override
        public int getItemCount() {
            return mRestaurants.size();
        }

        public void setRestaurants(List<Restaurant> restaurants) {
            mRestaurants = restaurants;
        }
    }
}

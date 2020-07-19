package com.example.restauranthealthinspectionbrowser.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.io.FileNotFoundException;
import java.util.List;

/**
 * RestaurantListFragment sets up the main screen with a list of restaurants.
 * through RestaurantManager class. It starts RestaurantActivity upon selection of a
 * restaurant in the list.
 */
public class RestaurantListFragment extends Fragment {
    private static final String TAG = "RestaurantListFragment";

    private RecyclerView mRestaurantRecyclerView;
    private RestaurantAdapter mAdapter;

    private RestaurantManager mRestaurantManager;
    private InspectionManager mInspectionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        try {
            mRestaurantManager = RestaurantManager.getInstance(getActivity());
            mInspectionManager = InspectionManager.getInstance(getActivity());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_restaurant_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_view:
                Intent intent = new Intent (getActivity(), MapsActivity.class);
                startActivity(intent);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
        List<Restaurant> restaurants = mRestaurantManager.getRestaurants();

        if (isAdded()) {
            mAdapter = new RestaurantAdapter(restaurants);
            mRestaurantRecyclerView.setAdapter(mAdapter);
        }
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
            mTitleTextView.setText(getString(R.string.restaurant_name, restaurant.getName()));

            Inspection inspection = mInspectionManager.getLatestInspection(mRestaurantID);
            if (inspection != null) {
                mDateTextView.setText(DateHelper.getDisplayDate(inspection.getInspectionDate()));

                int numIssues = inspection.getNumOfCritical() + inspection.getNumOfNonCritical();
                mNumIssuesTextView.setText(getString(R.string.num_issues, numIssues));

                String hazardLevel = inspection.getHazardRating();
                mHazardLevelTextView.setText(getString(R.string.hazard_level, hazardLevel));
                if (hazardLevel.equals("High")) {
                    mHazardLevelTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.highHazardLevel));
                    mHazardLevelImageView.setImageResource(R.drawable.red_exclamation_circle);
                } else if (hazardLevel.equals("Moderate")) {
                    mHazardLevelTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.moderateHazardLevel));
                    mHazardLevelImageView.setImageResource(R.drawable.yellow_exclamation_circle);
                } else {
                    mHazardLevelTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.lowHazardLevel));
                    mHazardLevelImageView.setImageResource(R.drawable.green_exclamation_circle);
                }
            }
            else {
                mDateTextView.setText("");
                mNumIssuesTextView.setText(R.string.no_inspection_info);
                mHazardLevelTextView.setText("");
                mHazardLevelImageView.setImageResource(R.drawable.blank_icon);
            }
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

package com.example.restauranthealthinspectionbrowser.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthealthinspectionbrowser.R;
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

        mRestaurantRecyclerView = (RecyclerView) view
                .findViewById(R.id.restaurant_recycler_view);
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

    private class RestaurantHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView mInfoTextView;
        private TextView mDateTextView;

        private Restaurant mRestaurant;

        public RestaurantHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_restaurant, parent, false));

            mTitleTextView = (TextView) itemView.findViewById(R.id.restaurant_name);
            mInfoTextView = (TextView) itemView.findViewById(R.id.inspection_info);
            mDateTextView = (TextView) itemView.findViewById(R.id.inspection_date);
        }

        public void bind(Restaurant restaurant) {
            mRestaurant = restaurant;
            mTitleTextView.setText(mRestaurant.getName());
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

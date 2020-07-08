package com.example.restauranthealthinspectionbrowser.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthealthinspectionbrowser.R;
import com.example.restauranthealthinspectionbrowser.model.Inspection;
import com.example.restauranthealthinspectionbrowser.model.InspectionManager;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;
import com.example.restauranthealthinspectionbrowser.model.RestaurantManager;

import java.util.List;

import static com.example.restauranthealthinspectionbrowser.ui.RestaurantActivity.EXTRA_RESTAURANT_ID;

public class RestaurantFragment extends Fragment {
    private TextView mTitleTextView;
    private TextView mAddressTextView;
    private TextView mCoordinatesTextView;

    private RecyclerView mInspectionRecyclerView;
    private InspectionAdapter mAdapter;

    private Restaurant mRestaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        String restaurantID = getActivity().getIntent().getStringExtra(EXTRA_RESTAURANT_ID);
        mRestaurant = RestaurantManager.getInstance(getActivity()).getRestaurant(restaurantID);

        mTitleTextView = (TextView) view.findViewById(R.id.title);
        mAddressTextView = (TextView) view.findViewById(R.id.address);
        mCoordinatesTextView = (TextView) view.findViewById(R.id.coordinates);

        mInspectionRecyclerView = (RecyclerView) view.findViewById(R.id.inspection_recycler_view);
        mInspectionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateTextViews();
        updateRecyclerView();

        return view;
    }

    private void updateTextViews() {
        mTitleTextView.setText(getString(R.string.restaurant_name, mRestaurant.getName()));
        mAddressTextView.setText(getString(R.string.address, mRestaurant.getAddress()));
        mCoordinatesTextView.setText(getString(R.string.coordinates, mRestaurant.getLatitude(), mRestaurant.getLongitude()));
    }

    private void updateRecyclerView() {
        InspectionManager manager = InspectionManager.getInstance(getActivity());
        List<Inspection> inspections = manager.getInspections();
        mAdapter = new InspectionAdapter(inspections);
        mInspectionRecyclerView.setAdapter(mAdapter);
    }

    private class InspectionHolder extends RecyclerView.ViewHolder {
        public InspectionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_inspection, parent, false));
        }
    }

    private class InspectionAdapter extends RecyclerView.Adapter<InspectionHolder> {
        private List<Inspection> mInspections;
        public InspectionAdapter(List<Inspection> inspections) {
            mInspections = inspections;
        }

        @Override
        public InspectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new InspectionHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(InspectionHolder holder, int position) {
        }
        @Override
        public int getItemCount() {
            return mInspections.size();
        }
    }
}

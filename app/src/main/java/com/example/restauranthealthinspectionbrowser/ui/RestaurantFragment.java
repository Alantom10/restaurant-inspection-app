package com.example.restauranthealthinspectionbrowser.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import static com.example.restauranthealthinspectionbrowser.ui.RestaurantActivity.EXTRA_RESTAURANT_ID;

public class RestaurantFragment extends Fragment {
    private TextView mTitleTextView;
    private TextView mAddressTextView;
    private TextView mCoordinatesTextView;

    private RecyclerView mInspectionRecyclerView;
    private InspectionAdapter mAdapter;

    private Restaurant mRestaurant;
    private String mRestaurantID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        mRestaurantID = getActivity().getIntent().getStringExtra(EXTRA_RESTAURANT_ID);
        mRestaurant = RestaurantManager.getInstance(getActivity()).getRestaurant(mRestaurantID);

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
        List<Inspection> inspections = manager.getInspectionsForRestaurant(mRestaurantID);
        mAdapter = new InspectionAdapter(inspections);
        mInspectionRecyclerView.setAdapter(mAdapter);
    }

    private class InspectionHolder extends RecyclerView.ViewHolder {
        private TextView mNumCriticalIssuesTextView;
        private TextView mNumNonCriticalIssuesTextView;
        private TextView mHazardLevelTextView;
        private TextView mDateTextView;
        private ImageView mHazardLevelImageView;

        private Inspection mInspection;

        public InspectionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_inspection, parent, false));

            mNumCriticalIssuesTextView = (TextView) itemView.findViewById(R.id.num_critical_issues);
            mNumNonCriticalIssuesTextView = (TextView) itemView.findViewById(R.id.num_non_critical_issues);
            mHazardLevelTextView = (TextView) itemView.findViewById(R.id.hazard_level);
            mDateTextView = (TextView) itemView.findViewById(R.id.inspection_date);
            mHazardLevelImageView = (ImageView) itemView.findViewById(R.id.hazard_level_icon);
        }

        public void bind(Inspection inspection) {
            mInspection = inspection;

            mNumCriticalIssuesTextView.setText(getString(R.string.num_critical_issues, mInspection.getNumOfCritical()));
            mNumNonCriticalIssuesTextView.setText(getString(R.string.num_non_critical_issues, mInspection.getNumOfNonCritical()));
            mDateTextView.setText(DateHelper.getDisplayDate(mInspection.getInspectionDate()));

            String hazardLevel = inspection.getHazardRating();
            mHazardLevelTextView.setText(getString(R.string.hazard_level, hazardLevel));
            if (hazardLevel.equals("High")) {
                mHazardLevelTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.highHazardLevel));
                mHazardLevelImageView.setImageResource(R.drawable.ic_high_level_black_24dp);
            } else if (hazardLevel.equals("Moderate")) {
                mHazardLevelTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.moderateHazardLevel));
                mHazardLevelImageView.setImageResource(R.drawable.ic_moderate_level_black_24dp);
            } else {
                mHazardLevelTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.lowHazardLevel));
                mHazardLevelImageView.setImageResource(R.drawable.ic_low_level_black_24dp);
            }
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
            Inspection inspection = mInspections.get(position);
            holder.bind(inspection);
        }
        @Override
        public int getItemCount() {
            return mInspections.size();
        }
    }
}

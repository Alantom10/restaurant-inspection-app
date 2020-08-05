package com.example.restauranthealthinspectionbrowser.ui;

import android.content.ContentValues;
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
import com.example.restauranthealthinspectionbrowser.databse.RestaurantDbSchema;
import com.example.restauranthealthinspectionbrowser.model.DateHelper;
import com.example.restauranthealthinspectionbrowser.model.Inspection;
import com.example.restauranthealthinspectionbrowser.model.InspectionManager;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;
import com.example.restauranthealthinspectionbrowser.model.RestaurantManager;

import java.io.FileNotFoundException;
import java.util.List;

import static com.example.restauranthealthinspectionbrowser.ui.RestaurantActivity.EXTRA_RESTAURANT_ID;

/**
 * RestaurantFragment sets up the Inspections screen with info on the restaurant
 * and a list of inspections. It starts InspectionActivity upon selection of an
 * inspection in the list.
 */
public class RestaurantFragment extends Fragment {
    private TextView mTitleTextView;
    private TextView mAddressTextView;
    private TextView mCoordinatesTextView;

    private RecyclerView mInspectionRecyclerView;
    private InspectionAdapter mAdapter;

    private Restaurant mRestaurant;
    private String mRestaurantID;

    public static final String RESTAURANT_LATITUDE_INTENT_TAG = "Restaurant latitude";
    public static final String RESTAURANT_LONGITUDE_INTENT_TAG = "Restaurant longitude";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        mRestaurantID = getActivity().getIntent().getStringExtra(EXTRA_RESTAURANT_ID);
        mRestaurant = new RestaurantManager(getActivity()).getRestaurant(mRestaurantID);

        mTitleTextView = (TextView) view.findViewById(R.id.title);
        mAddressTextView = (TextView) view.findViewById(R.id.address);
        mCoordinatesTextView = (TextView) view.findViewById(R.id.coordinates);

        mInspectionRecyclerView = (RecyclerView) view.findViewById(R.id.inspection_recycler_view);
        mInspectionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateTextViews();

        try {
            updateRecyclerView();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_restaurant, menu);
        MenuItem favorite = menu.findItem(R.id.action_favorite);

        favorite.setIcon(R.drawable.ic_baseline_star_24);

        if (mRestaurant.isFavorite()) {
            favorite.setIcon(R.drawable.ic_baseline_star_24);
        } else {
            favorite.setIcon(R.drawable.ic_baseline_star_border_24);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_favorite) {

        }

        return super.onOptionsItemSelected(item);
    }

    private void updateTextViews() {
        mTitleTextView.setText(getString(R.string.restaurant_name, mRestaurant.getTitle()));
        mAddressTextView.setText(getString(R.string.address, mRestaurant.getAddress()));
        mCoordinatesTextView.setText(getString(R.string.coordinates, mRestaurant.getLatitude(), mRestaurant.getLongitude()));

        mCoordinatesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MapsActivity.makeIntent(getActivity());
                intent.putExtra(RESTAURANT_LATITUDE_INTENT_TAG, mRestaurant.getLatitude());
                intent.putExtra(RESTAURANT_LONGITUDE_INTENT_TAG, mRestaurant.getLongitude());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void updateRecyclerView() throws FileNotFoundException {
        InspectionManager manager = InspectionManager.getInstance(getActivity());
        List<Inspection> inspections = manager.getInspectionsForRestaurant(mRestaurantID);
        mAdapter = new InspectionAdapter(inspections);
        mInspectionRecyclerView.setAdapter(mAdapter);
    }

    private class InspectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNumCriticalIssuesTextView;
        private TextView mNumNonCriticalIssuesTextView;
        private TextView mHazardLevelTextView;
        private TextView mDateTextView;
        private ImageView mHazardLevelImageView;

        private Inspection mInspection;

        public InspectionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_inspection, parent, false));
            itemView.setOnClickListener(this);

            mNumCriticalIssuesTextView = (TextView) itemView.findViewById(R.id.num_critical_issues);
            mNumNonCriticalIssuesTextView = (TextView) itemView.findViewById(R.id.num_non_critical_issues);
            mHazardLevelTextView = (TextView) itemView.findViewById(R.id.hazard_level);
            mDateTextView = (TextView) itemView.findViewById(R.id.inspection_date);
            mHazardLevelImageView = (ImageView) itemView.findViewById(R.id.hazard_level_icon);
        }

        public void bind(Inspection inspection) {
            mInspection = inspection;

            mNumCriticalIssuesTextView.setText(getString(R.string.num_critical_issues, mInspection.getNumCritical()));
            mNumNonCriticalIssuesTextView.setText(getString(R.string.num_non_critical_issues, mInspection.getNumNonCritical()));
            mDateTextView.setText(DateHelper.getDisplayDate(mInspection.getInspectionDate()));

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

        @Override
        public void onClick(View v) {
            Intent intent = InspectionActivity.makeIntent(getActivity(), mInspection);
            startActivity(intent);
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

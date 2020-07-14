package com.example.restauranthealthinspectionbrowser.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthealthinspectionbrowser.R;
import com.example.restauranthealthinspectionbrowser.model.DataFetcher;
import com.example.restauranthealthinspectionbrowser.model.DateHelper;
import com.example.restauranthealthinspectionbrowser.model.Inspection;
import com.example.restauranthealthinspectionbrowser.model.InspectionManager;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;
import com.example.restauranthealthinspectionbrowser.model.RestaurantManager;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * RestaurantListFragment sets up the main screen with a list of restaurants.
 * through RestaurantManager class. It starts RestaurantActivity upon selection of a
 * restaurant in the list.
 */
public class RestaurantListFragment extends Fragment {
    public static final String FILE_NAME_RESTAURANTS = "restaurants.csv";
    public static final String FILE_NAME_INSPECTION_REPORTS = "inspection_reports.csv";
    private static final String PREFERENCES = "restaurant list";
    private static final String PREFERENCES_LAST_UPDATED = "last updated";
    private static final String PREFERENCES_LAST_MODIFIED_RESTAURANTS = "last modified restaurants";
    private static final String PREFERENCES_LAST_MODIFIED_INSPECTIONS = "last modified inspections";
    private static final String DIALOG_UPDATE = "dialog update";
    private static final String TAG = "RestaurantListFragment";

    private static final int REQUEST_DOWNLOAD = 11;

    private RecyclerView mRestaurantRecyclerView;
    private RestaurantAdapter mAdapter;

    private RestaurantManager mRestaurantManager;
    private InspectionManager mInspectionManager;

    private String mNewLastModifiedRestaurants;
    private String mNewLastModifiedInspections;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        try {
            mRestaurantManager = RestaurantManager.getInstance(getActivity());
            mInspectionManager = InspectionManager.getInstance(getActivity());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        SharedPreferences sp = getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong(PREFERENCES_LAST_UPDATED, 0);
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdated > TimeUnit.HOURS.toMillis(20)) {
            new FetchLastModifiedTask().execute();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DOWNLOAD) {
            new FetchDataTask().execute();
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

    private class FetchLastModifiedTask extends AsyncTask<Void,Void,String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            String[] lastModified = {null, null};
            try {
                lastModified[0] = new DataFetcher().fetchLastModifiedRestaurants();
                lastModified[1] = new DataFetcher().fetchLastModifiedInspections();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lastModified;
        }

        @Override
        protected void onPostExecute(String[] newLastModified) {
            mNewLastModifiedRestaurants = newLastModified[0];
            mNewLastModifiedInspections = newLastModified[1];

            SharedPreferences sp = getActivity()
                    .getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            String lastModifiedRestaurants = sp.getString(PREFERENCES_LAST_MODIFIED_RESTAURANTS, "");
            String lastModifiedInspections = sp.getString(PREFERENCES_LAST_MODIFIED_INSPECTIONS, "");
            if (!mNewLastModifiedRestaurants.equals(lastModifiedRestaurants) ||
                    !mNewLastModifiedInspections.equals(lastModifiedInspections))
            {
                FragmentManager manager = getFragmentManager();
                DataUpdateFragment dialog = new DataUpdateFragment();
                dialog.setTargetFragment(RestaurantListFragment.this, REQUEST_DOWNLOAD);
                dialog.show(manager, DIALOG_UPDATE);
            }
        }
    }

    private class FetchDataTask extends AsyncTask<Void,Integer,Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.downloading));
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            try {
                DataFetcher dataFetcher = new DataFetcher();
                byte[] restaurantData = dataFetcher.fetchRestaurantData();
//                Log.i(TAG, "Downloaded restaurant.csv: " + restaurantData);
                storeData(FILE_NAME_RESTAURANTS, restaurantData);

                publishProgress(50);

                byte[] inspectionData = dataFetcher.fetchInspectionData();
                storeData(FILE_NAME_INSPECTION_REPORTS, inspectionData);

                publishProgress(100);

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void onProgressUpdate(Integer... progress) {
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

            try {
                mRestaurantManager.updateRestaurants(getActivity());
                mInspectionManager.updateInspections(getActivity());

                SharedPreferences sp = getActivity()
                        .getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(PREFERENCES_LAST_MODIFIED_RESTAURANTS, mNewLastModifiedRestaurants);
                editor.putString(PREFERENCES_LAST_MODIFIED_INSPECTIONS, mNewLastModifiedInspections);
                editor.putLong(PREFERENCES_LAST_UPDATED, System.currentTimeMillis());
                editor.apply();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            updateUI();
        }

        private void storeData(String fileName, byte[] data) throws IOException {
            OutputStream outputStream = getActivity()
                    .openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data);
            outputStream.close();
        }
    }
}

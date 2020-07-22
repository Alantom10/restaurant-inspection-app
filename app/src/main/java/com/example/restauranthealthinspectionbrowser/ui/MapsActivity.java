package com.example.restauranthealthinspectionbrowser.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restauranthealthinspectionbrowser.R;

import com.example.restauranthealthinspectionbrowser.model.DataFetcher;
import com.example.restauranthealthinspectionbrowser.model.Inspection;
import com.example.restauranthealthinspectionbrowser.model.InspectionManager;
import com.example.restauranthealthinspectionbrowser.model.PegItem;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;
import com.example.restauranthealthinspectionbrowser.model.RestaurantManager;
import com.example.restauranthealthinspectionbrowser.model.SimpleClusterRender;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";

    public static final String FILE_NAME_RESTAURANTS = "restaurants.csv";
    public static final String FILE_NAME_INSPECTION_REPORTS = "inspection_reports.csv";

    private String mNewLastModifiedRestaurants;
    private String mNewLastModifiedInspections;

    private RestaurantManager mRestaurantManager;
    private InspectionManager mInspectionManager;
    private DataPackageManager mDataPackageManager;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private EditText mSearchText;
    private ClusterManager<PegItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mSearchText = (EditText) findViewById(R.id.editSearch);
        getLocationPermission();
        setItemOnClick();
        initSearch();

            mRestaurantManager = RestaurantManager.getInstance(this);
            mInspectionManager = InspectionManager.getInstance(this);


        mDataPackageManager = DataPackageManager.getInstance(this);

        if (!mDataPackageManager.isHasRequestedDownloadPermission()) {
            long lastUpdated = mDataPackageManager.getLastUpdated();
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdated > TimeUnit.HOURS.toMillis(20)) {
                new FetchLastModifiedTask().execute();
            }
        }
    }

    // Adapted from: https://www.youtube.com/watch?v=Vt6H9TOmsuo&list=PLgCYzUzKIBE-vInwQhGSdnbyJ62nixHCt&index=4
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        setUpClusterer();

        mMap.setInfoWindowAdapter(new MapInfoWindowAdapter(MapsActivity.this));
        setOnMapsListener();

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize the map
                    initMap();
                }
            }
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation:getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG,"onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),
                                    currentLocation.getLongitude()), DEFAULT_ZOOM);
                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch(SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        //drop a pin
        //MarkerOptions options = new MarkerOptions().position(latLng);
        //mMap.addMarker(options);

        hideKeyboard();
    }

    private void initSearch(){
        //override ENTER key as getting result
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == event.ACTION_DOWN
                        || event.getAction() == event.KEYCODE_ENTER) {
                    geoLocate();
                }
                return false;
            }
        });
        hideKeyboard();
    }

    private void geoLocate() {
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.e(TAG, "getLocate: IOException: " + e.getMessage());
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.e(TAG,"geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard(){
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    //The above are all adapted from the youtube video:
    //https://www.youtube.com/watch?v=Vt6H9TOmsuo&list=PLgCYzUzKIBE-vInwQhGSdnbyJ62nixHCt&index=4

    private void setItemOnClick(){
        ImageView list = findViewById(R.id.ic_listview);
        list.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MapsActivity.this, RestaurantListActivity.class);
                startActivity(intent);
            }
        });

        ImageView location = findViewById(R.id.ic_my_location);
        location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
    }

    //Adapted from: https://developers.google.com/maps/documentation/android-sdk/utility/marker-clustering
        private void setUpClusterer() {
        // Initialize new clusterManager
            mClusterManager = new ClusterManager<PegItem>(this, mMap);

            SimpleClusterRender markerClusterRenderer = new SimpleClusterRender(this, mMap, mClusterManager); // 2
            mClusterManager.setRenderer(markerClusterRenderer);

            mMap.setOnCameraIdleListener(mClusterManager);
//        mMap.setOnMarkerClickListener(mClusterManager);

                addItems();

            mClusterManager.cluster();
        }

    private void addItems() {
        RestaurantManager manager = RestaurantManager.getInstance(getBaseContext());
        List<Restaurant> restaurants = manager.getRestaurants();

        InspectionManager inspectionManager = InspectionManager.getInstance(this);

        int i = 0;
        for (Restaurant restaurant : restaurants) {
            String name = restaurant.getName();
            String mRestaurantID = restaurant.getID();
            Inspection inspection = inspectionManager.getLatestInspection(mRestaurantID);
            String hazardLevel = "";

            if (inspection != null) {
                hazardLevel = inspection.getHazardRating();
            }

            PegItem peg = new PegItem(restaurant.getLatitude(),
                    restaurant.getLongitude(),
                    name, hazardLevel);

            mClusterManager.addItem(peg);
        }
    }

    private void setOnMapsListener() {
        /**
         *
         */
        this.mMap.setOnMarkerClickListener((marker) -> {
            moveCamera(marker.getPosition(), DEFAULT_ZOOM);
            marker.showInfoWindow();
            return true;
        });

        mMap.setOnInfoWindowClickListener(marker -> {
            LatLng position = marker.getPosition();

            Restaurant restaurant = RestaurantManager.getInstance(MapsActivity.this).getRestaurant(position);

            if (restaurant == null) {
                return;
            }

            Intent intent = new Intent(MapsActivity.this, RestaurantActivity.class);
            intent.putExtra(RestaurantActivity.EXTRA_RESTAURANT_ID,restaurant.getID());
            startActivity(intent);
        });


        mMap.setOnMapClickListener((latLng) -> {
            mClusterManager.clearItems();
            mMap.clear();

            setUpClusterer();

            moveCamera(latLng, 15f);
        });


        mClusterManager.setOnClusterClickListener((cluster) -> {
            moveCamera(cluster.getPosition(), -10f);
            return true;
        });

    }

    //----------------------------------------------------------------------------------------------

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

            String lastModifiedRestaurants = mDataPackageManager.getLastModifiedRestaurants();
            String lastModifiedInspections = mDataPackageManager.getLastModifiedInspections();
            if (!mNewLastModifiedRestaurants.equals(lastModifiedRestaurants) ||
                    !mNewLastModifiedInspections.equals(lastModifiedInspections))
            {
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle(R.string.data_update_title)
                        .setMessage(R.string.data_update_message)
                        .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDataPackageManager.setHasRequestedDownloadPermission(true);
                                new FetchDataTask().execute();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDataPackageManager.setHasRequestedDownloadPermission(true);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    private class FetchDataTask extends AsyncTask<Void,Integer,byte[][]> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setMessage(getString(R.string.downloading));
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                    getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancel(true);
                        }
                    });
            progressDialog.show();
        }


        @Override
        protected byte[][] doInBackground(Void... params) {
            byte[][] dataArrays = { null, null };

            try {
                DataFetcher dataFetcher = new DataFetcher();
                byte[] restaurantData = dataFetcher.fetchRestaurantData();
//                Log.i(TAG, "Downloaded restaurant.csv: " + restaurantData);
                dataArrays[0] = restaurantData;

                if (isCancelled()) {
                    return null;
                }

                publishProgress(50);

                byte[] inspectionData = dataFetcher.fetchInspectionData();
                dataArrays[1] = inspectionData;

                if (isCancelled()) {
                    return null;
                }

                publishProgress(100);

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return dataArrays;
        }

        public void onProgressUpdate(Integer... progress) {
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(byte[][] arrays) {
            progressDialog.dismiss();

            if (arrays == null || arrays[0] == null || arrays[1] == null) {
                return;
            }

            try {
                storeData(FILE_NAME_RESTAURANTS, arrays[0]);
                storeData(FILE_NAME_INSPECTION_REPORTS, arrays[1]);

                mRestaurantManager.updateRestaurants(MapsActivity.this);
                mInspectionManager.updateInspections(MapsActivity.this);

                mDataPackageManager.updateLastModified(
                        MapsActivity.this,
                        mNewLastModifiedRestaurants,
                        mNewLastModifiedInspections);

            } catch (IOException e) {
                e.printStackTrace();
            }

//            updateUI();
        }

        private void storeData(String fileName, byte[] data) throws IOException {
            OutputStream outputStream = openFileOutput(fileName, MODE_PRIVATE);
            outputStream.write(data);
            outputStream.close();
        }
    }
}
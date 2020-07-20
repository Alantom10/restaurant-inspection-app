package com.example.restauranthealthinspectionbrowser.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restauranthealthinspectionbrowser.R;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;
import com.example.restauranthealthinspectionbrowser.model.RestaurantManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.FileNotFoundException;

public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private final Context context;
    private Restaurant restaurant;

    public MapInfoWindowAdapter(Context context){
        this.context = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.fragment_map_info_pop_window, null);
    }

    private void renderWindowText(Marker marker, View view){
        TextView restaurantName = (TextView) view.findViewById(R.id.txtMapResName);
        TextView restaurantAddress = (TextView) view.findViewById(R.id.txtMapAddress);
        ImageView restaurantIcon = (ImageView) view.findViewById(R.id.imageMapIcon);
        ImageView restaurantHazard = (ImageView) view.findViewById(R.id.imageMapHazard);


            this.restaurant = RestaurantManager.getInstance(context).getRestaurant(marker.getPosition());

        if(this.restaurant == null) {
            return;
        }
        restaurantName.setText(restaurant.getName());
        restaurantAddress.setText(restaurant.getAddress());
        restaurantIcon.setImageResource(restaurant.getHazardIcon());
        restaurantHazard.setImageResource(restaurant.getHazardIcon()); //todo
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     * only be called when getInfoWindow(Marker) returns null
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return  mWindow;
    }

}

package com.example.restauranthealthinspectionbrowser.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.restauranthealthinspectionbrowser.R;
import com.example.restauranthealthinspectionbrowser.model.HazardRatingHelper;
import com.example.restauranthealthinspectionbrowser.model.Inspection;
import com.example.restauranthealthinspectionbrowser.model.InspectionManager;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;
import com.example.restauranthealthinspectionbrowser.model.RestaurantIconHelper;
import com.example.restauranthealthinspectionbrowser.model.RestaurantManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * An adapter class used by MapsActivity to set up restaurant info window.
 */
public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private final Context mContext;

    public MapInfoWindowAdapter(Context context){
        this.mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.fragment_map_info_window, null);
    }

    private void renderWindowText(Marker marker, View view){
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        TextView addressTextView = (TextView) view.findViewById(R.id.address);
        TextView hazardLevelTextView = (TextView) view.findViewById(R.id.hazard_level);
        ImageView restaurantIcon = (ImageView) view.findViewById(R.id.restaurant_icon);
        ImageView hazardLevelIcon = (ImageView) view.findViewById(R.id.hazard_level_icon);

        Restaurant restaurant = RestaurantManager.getInstance(mContext).getRestaurant(marker.getPosition());

        if(restaurant == null) {
            return;
        }

        titleTextView.setText(restaurant.getTitle());
        addressTextView.setText(restaurant.getAddress());

        int iconResId = new RestaurantIconHelper().getIconResId(restaurant.getTitle());
        restaurantIcon.setImageResource(iconResId);

        String restaurantID = restaurant.getId();
        Inspection inspection = InspectionManager.getInstance(mContext).getLatestInspection(restaurantID);

        if (inspection != null) {
            String hazardLevel = inspection.getHazardRating();
            hazardLevelTextView.setText(mContext.getString(R.string.hazard_level, hazardLevel));

            HazardRatingHelper helper = new HazardRatingHelper();
            hazardLevelTextView.setTextColor(ContextCompat.getColor(mContext, helper.getHazardColor(hazardLevel)));
            hazardLevelIcon.setImageResource(helper.getHazardIcon(hazardLevel));
        }
        else {
            hazardLevelTextView.setText(R.string.no_inspection_info);
            hazardLevelIcon.setImageResource(R.drawable.blank_icon);
        }
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

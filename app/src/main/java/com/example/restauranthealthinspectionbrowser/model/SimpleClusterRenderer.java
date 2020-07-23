package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;

import com.example.restauranthealthinspectionbrowser.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * A helper class for formatting images displayed on map screen.
 */
public class SimpleClusterRenderer extends DefaultClusterRenderer<PegItem> {
    public SimpleClusterRenderer(Context context, GoogleMap map, ClusterManager<PegItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(PegItem item, MarkerOptions markerOptions) {
        BitmapDescriptor bitmapDescriptor = null;
        if("Low".equalsIgnoreCase(item.getHazardLevel())){
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.green_peg);
        }
        else if("Moderate".equalsIgnoreCase(item.getHazardLevel())){
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.yellow_peg);
        }
        else if("High".equalsIgnoreCase(item.getHazardLevel())){
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.orange_peg);
        }
        else{
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.grey_peg);
        }
        markerOptions.title(item.getTitle()).icon(bitmapDescriptor);

        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}

package com.example.restauranthealthinspectionbrowser.model;

import android.content.Context;

import com.example.restauranthealthinspectionbrowser.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class SimpleClusterRender extends DefaultClusterRenderer<PegItem> {
    public SimpleClusterRender(Context context, GoogleMap map, ClusterManager<PegItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(PegItem item, MarkerOptions markerOptions) {
        BitmapDescriptor bitmapDescriptor=null;
        if("Low".equalsIgnoreCase(item.getmHazardLevel())){
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.yellow);
        }
        if("high".equalsIgnoreCase(item.getmHazardLevel())){
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.yellow);
        }
        markerOptions.title(item.getTitle()).icon(bitmapDescriptor);

        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}

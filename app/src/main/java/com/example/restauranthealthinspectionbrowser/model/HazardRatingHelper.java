package com.example.restauranthealthinspectionbrowser.model;

import androidx.core.content.ContextCompat;

import com.example.restauranthealthinspectionbrowser.R;

/**
 * A helper class for getting UI resources related to restaurant hazard ratings.
 */
public class HazardRatingHelper {

    public int getHazardColor(String hazardRating) {
        if (hazardRating.equals("High")) {
            return R.color.highHazardLevel;
        } else if (hazardRating.equals("Moderate")) {
            return R.color.moderateHazardLevel;
        } else {
            return R.color.lowHazardLevel;
        }
    }

    public int getHazardIcon(String hazardRating) {
        if (hazardRating.equals("High")) {
            return R.drawable.red_exclamation_circle;
        } else if (hazardRating.equals("Moderate")) {
            return R.drawable.yellow_exclamation_circle;
        } else {
            return R.drawable.green_exclamation_circle;
        }
    }
}

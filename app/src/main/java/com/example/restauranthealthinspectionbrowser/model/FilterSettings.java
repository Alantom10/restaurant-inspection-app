package com.example.restauranthealthinspectionbrowser.model;

public class FilterSettings {
    private static FilterSettings filterSettings = null;
    private boolean favSetting;
    private String hazardSetting;
    private int minSetting;
    private int maxSetting;

    private FilterSettings() {

    }

    public static FilterSettings getFilterSettings() {
        if(filterSettings == null) {
            filterSettings = new FilterSettings();
            return filterSettings;
        }
        else {
            return filterSettings;
        }
    }

    public boolean isFavSetting() {
        return favSetting;
    }

    public void setFavSetting(boolean favSetting) {
        this.favSetting = favSetting;
    }

    public String getHazardSetting() {
        return hazardSetting;
    }

    public void setHazardSetting(String hazardSetting) {
        this.hazardSetting = hazardSetting;
    }

    public int getMinSetting() {
        return minSetting;
    }

    public void setMinSetting(int minSetting) {
        this.minSetting = minSetting;
    }

    public int getMaxSetting() {
        return maxSetting;
    }

    public void setMaxSetting(int maxSetting) {
        this.maxSetting = maxSetting;
    }
}

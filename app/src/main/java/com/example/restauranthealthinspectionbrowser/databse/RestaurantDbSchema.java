package com.example.restauranthealthinspectionbrowser.databse;

public class RestaurantDbSchema {
    public static final class RestaurantTable {
        public static final String NAME = "restaurants";

        public static final class Cols {
            public static final String ID = "id";
            public static final String TITLE = "title";
            public static final String ISSUES = "number of issues";
            public static final String HAZARD_LEVEL = "hazard level";
            public static final String DATE = "date";
            public static final String FAVOURITE = "favourite";
        }
    }
}

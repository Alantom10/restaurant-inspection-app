package com.example.restauranthealthinspectionbrowser.databse;

/**
 * RestaurantDbSchema provides a schema for restaurant database.
 */
public class RestaurantDbSchema {
    public static final class RestaurantTable {
        public static final String NAME = "restaurants";

        public static final class Cols {
            public static final String ID = "id";
            public static final String TITLE = "title";
            public static final String ADDRESS = "address";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String ISSUES = "issues";
            public static final String RATING = "rating";
            public static final String DATE = "date";
            public static final String FAVOURITE = "favourite";
            public static final String UPDATED = "updated";
            public static final String CRITICAL = "critical_issues";
        }
    }
}

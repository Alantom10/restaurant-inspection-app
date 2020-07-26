package com.example.restauranthealthinspectionbrowser.databse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restauranthealthinspectionbrowser.databse.RestaurantDbSchema.RestaurantTable;

public class RestaurantBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "restaurantBase.db";

    public RestaurantBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RestaurantTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                RestaurantTable.Cols.ID + ", " +
                RestaurantTable.Cols.TITLE + ", " +
                RestaurantTable.Cols.ADDRESS + ", " +
                RestaurantTable.Cols.LATITUDE + ", " +
                RestaurantTable.Cols.LONGITUDE + ", " +
                RestaurantTable.Cols.ISSUES + ", " +
                RestaurantTable.Cols.RATING + ", " +
                RestaurantTable.Cols.DATE + ", " +
                RestaurantTable.Cols.FAVOURITE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

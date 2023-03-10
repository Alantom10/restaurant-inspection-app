package com.example.restauranthealthinspectionbrowser.model;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A helper class that converts Date objects to formatted strings for display
 * on UI.
 */
public class DateHelper {
    private static final String TAG = "DateHelper";

    public static CharSequence getDisplayDate(Date inspectionDate) {
        long time = inspectionDate.getTime();
        long now = System.currentTimeMillis();
        long interval = now - time;
        Log.i(TAG, "Days ago: " + (interval < TimeUnit.DAYS.toMillis(30)));
        if (interval < TimeUnit.DAYS.toMillis(30)) {
            return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.DAY_IN_MILLIS);
        }
        else if (interval < DateUtils.YEAR_IN_MILLIS) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.CANADA);
            return sdf.format(time);
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.CANADA);
            return sdf.format(time);
        }
    }

    public static CharSequence getFullDate(Date inspectionDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.CANADA);
        return sdf.format(inspectionDate);
    }

    public static Date parseInspectionDate(String inspectionDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CANADA);
        return sdf.parse(inspectionDate);
    }
}


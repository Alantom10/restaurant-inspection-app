package com.example.restauranthealthinspectionbrowser.model;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateHelper {

    public static CharSequence getDisplayDate(Date inspectionDate) {
        long time = inspectionDate.getTime();
        long now = System.currentTimeMillis();
        long interval = now - time;
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
}


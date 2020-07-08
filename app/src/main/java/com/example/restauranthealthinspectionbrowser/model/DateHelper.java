package com.example.restauranthealthinspectionbrowser.model;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateHelper {

    public static CharSequence getDisplayDate(String inspectionDate) {
        CharSequence result = null;
        try {
            SimpleDateFormat sdf_parse = new SimpleDateFormat("yyyyMMdd", Locale.CANADA);
            long time = sdf_parse.parse(inspectionDate).getTime();
            long now = System.currentTimeMillis();
            long interval = now - time;
            if (interval < TimeUnit.DAYS.toMillis(30)) {
                result = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.DAY_IN_MILLIS);
            }
            else if (interval < DateUtils.YEAR_IN_MILLIS) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.CANADA);
                result = sdf.format(time);
            }
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.CANADA);
                result = sdf.format(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static CharSequence getFullDate(String inspectionDate) {
        CharSequence result = null;
        try {
            SimpleDateFormat sdf_parse = new SimpleDateFormat("yyyyMMdd", Locale.CANADA);
            Date date = sdf_parse.parse(inspectionDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.CANADA);
            result = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}


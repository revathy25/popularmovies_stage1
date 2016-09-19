
package com.example.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
    public static final String DETAIL_PAGE_RELEASE_DATE_FORMAT = "MM.dd/yy";
    public static final String YEAR_DATE_FORMAT = "yyyy";

    public static String getPreferredSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default));
    }

    public static String getDetailPageFormattedDate(String incomingDateString, String dateFormat ) {

        Date date = null;
        try {
            date = new SimpleDateFormat(dateFormat).parse(incomingDateString);
            String formattedDate = new SimpleDateFormat(DETAIL_PAGE_RELEASE_DATE_FORMAT).format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String getYearFromDate(String incomingDateString, String dateFormat ) {

        Date date = null;
        try {
            date = new SimpleDateFormat(dateFormat).parse(incomingDateString);
            String formattedDate = new SimpleDateFormat(YEAR_DATE_FORMAT).format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

}
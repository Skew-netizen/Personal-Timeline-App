package com.example.timelinelogging.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static String getCurrentTime() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(new Date());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
    }

}

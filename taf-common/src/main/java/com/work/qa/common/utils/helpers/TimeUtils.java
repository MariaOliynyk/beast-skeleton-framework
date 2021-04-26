package com.work.qa.common.utils.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static long getCurrentDateTimeAsUnixTimeStamp()
    {
        return System.currentTimeMillis();
    }

    public static long getCreatedDateAsUnixTimeStamp(String datetime) {
        return getDate(datetime).getTime();
    }

    public static Date getDate(String datetime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = null;
        try {
            date = format.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        Date now = new Date();
        return dateFormat.format(now);
    }

    public static void sleep(int seconds)
    {
        long date = new Date().getTime() + (seconds * 1000);
        while (new Date().getTime() <=date) {}
    }
}

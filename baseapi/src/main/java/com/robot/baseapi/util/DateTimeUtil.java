package com.robot.baseapi.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lny on 2017/11/20.
 */

public class DateTimeUtil {

    public static final String data_format_str_1 = "yyyy-MM-dd HH:mm";
    public static final String data_format_str_2 = "yyyy年MM月dd日 HH:mm";
    public static final String data_format_str_3 = "yyyy-MM-dd HH:mm:ss";
    public static final String time_format_str_1 = "HH:mm:ss";

    /**
     * 将字符串转化为日历对象
     * default format yyyy-MM-dd HH:mm
     * @param dateTime
     * @return
     */
    public static Calendar text2Calendar(String dateTime) {
        if (TextUtils.isEmpty(dateTime)) {
            return null;
        } else {
            //2017-11-06 15:30:00
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date d = format.parse(dateTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d);
                return calendar;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static Calendar text2Calendar(String dateTime,String foramtStr) {
        if (TextUtils.isEmpty(dateTime)) {
            return null;
        } else {
            //2017-11-06 15:30:00
            SimpleDateFormat format = new SimpleDateFormat(foramtStr);
            try {
                Date d = format.parse(dateTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d);
                return calendar;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



    /**
     * 将字符串转化为日历对象
     * default formatStr   yyyy-MM-dd HH:mm
     *
     * @param
     * @return
     */
    public static String formatDateTime(long timeMillis) {
        //2017-11-06 15:30:00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            String str = format.format(timeMillis);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String formatDateTime(long timeMillis,String formatStr) {
        //2017-11-06 15:30:00
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            String str = format.format(timeMillis);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     *
     * @param dateTimeStr
     * @return  error -1
     */
    public static long dateString2Long(String dateTimeStr) {
        try {
            //2017-11-07 15:30:00
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateTimeStr);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static long dateString2Long(String dateTimeStr,String foramtStr) {
        try {
            //2017-11-07 15:30:00
            SimpleDateFormat sdf = new SimpleDateFormat(foramtStr);
            Date date = sdf.parse(dateTimeStr);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param dateStr
     * @return
     */
    public static long timeString2Long(String dateStr) {
        try {
            //2017-11-07 15:30:00
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = sdf.parse(dateStr);
            Calendar old = Calendar.getInstance();
            old.setTime(date);
            Calendar now = Calendar.getInstance();
            old.set(Calendar.YEAR, now.get(Calendar.YEAR));
            old.set(Calendar.MONTH, now.get(Calendar.MONTH));
            old.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
            return old.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }



    public static long timeString2Long(String dateStr,String formatStr) {
        try {
            //2017-11-07 15:30:00
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            Date date = sdf.parse(dateStr);
            Calendar old = Calendar.getInstance();
            old.setTime(date);
            Calendar now = Calendar.getInstance();
            old.set(Calendar.YEAR, now.get(Calendar.YEAR));
            old.set(Calendar.MONTH, now.get(Calendar.MONTH));
            old.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
            return old.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @param ms
     * @return
     */
    public static String formateDatetime(long ms) {
        try {
            //2017-11-07 15:30:00
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(new Date(ms));
            return format;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formateDatetime(long ms,String formatStr) {
        try {
            //2017-11-07 15:30:00
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            String format = sdf.format(new Date(ms));
            return format;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }








}

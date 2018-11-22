package com.robot.baseapi.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class SPManager {

    public static final int DEAFULT = 0;

    private static SharedPreferences sp;

    public static void ini(Context context) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    synchronized public static void put(String key, String value) {
        sp.edit().putString(key, value).commit();
    }

    synchronized public static void put(String key, boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    synchronized public static void put(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    synchronized public static void put(String key, long value) {
        sp.edit().putLong(key, value).commit();
    }

    synchronized public static void put(String key, float value) {
        sp.edit().putFloat(key, value).commit();
    }


    public static String get(String key) {
        return sp.getString(key, null);
    }

    public static boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean _default) {
        return sp.getBoolean(key, _default);
    }

    public static String getString(String key) {
        return sp.getString(key, "");
    }

    public static int getInt(String key) {
        return sp.getInt(key, DEAFULT);
    }

    public static int getInt(String key, int default_int) {
        return sp.getInt(key, default_int);
    }

    public static long getLong(String key) {
        return sp.getLong(key, DEAFULT);
    }

    public static float getFloat(String key,float default_float){
        return sp.getFloat(key,default_float);
    }
    public static float getFloat(String key){
        return sp.getFloat(key,DEAFULT);
    }



}

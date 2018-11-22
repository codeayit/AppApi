package com.robot.baseapi.util;

import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by lny on 2017/10/16.
 */

public class DeviceUtil {
    /**
     * 获取设备的mac地址，注意：要打开wifi,否则获取失败
     *
     * @param context
     * @return
     */
    public static String getDeviceMacAddress(Context context) {
        //wifi mac地址
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String wifiMac = info.getMacAddress();
//        return wifiMac;
        return "123456";
    }

    /**
     * 获取设备序列号
     *
     * @return
     */
    public static String getDeviceSerial() {
        return Build.SERIAL;
    }

    public static int getDevice_OS_SDK_INT() {
        return Build.VERSION.SDK_INT;
    }


    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
//        KLog.d("screen width : "+width);
        return width;
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
//        KLog.d("screen height : "+height);
        return height;
    }


    /**
     * 获取音量的百分比
     * @param context
     * @return
     */
    public static float getVolumePercent(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return Float.valueOf(currentVolume)/Float.valueOf(maxVolume);
    }

    /**
     * 获取系统亮度
     * @param context
     * @return
     */
    public static float getBrightnessPercent(Context context){
        int systemBrightness = 0;
                try {
                       systemBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                } catch (Settings.SettingNotFoundException e) {
                       e.printStackTrace();
                  }
               return Float.valueOf(systemBrightness)/255f;
    }


}

package com.robot.baseapi.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    /**
     * 返回网络状态
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) { 
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
                .getSystemService(Context.CONNECTIVITY_SERVICE); 
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
            if (mNetworkInfo != null) { 
                return mNetworkInfo.isAvailable(); 
            } 
        } 
        return false; 
    }
    
    /**
     * 返回当前Wifi是否连接上
     * @param context
     * @return true 已连接
     */
    public static boolean isWifiConnected(Context context){
    	ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMan.getActiveNetworkInfo();
		if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI){
			return true;
		}
		return false;
    }

    /**
     * 判断MOBILE网络是否可用
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isMobileDataEnable(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileDataEnable = false;
        isMobileDataEnable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        return isMobileDataEnable;
    }


    public static  boolean isInternet(Context context)
    {
        boolean result = false;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
        {
            result = false;
        }
        else
        {
            if (!info.isAvailable())
            {
                result =false;
            }
            else
            {
                result = true;
            }
        }

        return result;
    }

	
}

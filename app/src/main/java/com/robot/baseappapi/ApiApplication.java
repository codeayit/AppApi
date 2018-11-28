package com.robot.baseappapi;


import android.widget.Toast;

import com.ayit.klog.KLog;
import com.robot.baseapi.base.BaseApplication;
import com.robot.baseapi.net.GlobalFilter;
import com.robot.baseapi.net.NetWork;

import java.util.HashMap;

/**
 * Created by lny on 2018/1/15.
 */

public class ApiApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        NetWork.setGlobalFilter(new GlobalFilter() {
            @Override
            public void onPreResponse(int code, String json) {
                KLog.d("全局code 过滤： "+code);
                Toast.makeText(getApplicationContext(),"code:"+code,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPreRequest(String url, HashMap<String, String> headers, HashMap<String, String> params) {

            }

        });
    }
}

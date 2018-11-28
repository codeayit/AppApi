package com.robot.baseappapi;


import android.widget.Toast;

import com.ayit.klog.KLog;
import com.robot.baseapi.base.BaseApplication;
import com.robot.baseapi.net.GlobalFilter;
import com.robot.baseapi.net.NetWork;

/**
 * Created by lny on 2018/1/15.
 */

public class ApiApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        NetWork.setGlobalCodeFilter(new GlobalFilter() {
            @Override
            public void onCode(int code,String json) {
                KLog.d("全局code 过滤： "+code);
                Toast.makeText(getApplicationContext(),"code:"+code,Toast.LENGTH_LONG).show();
            }
        });
    }
}

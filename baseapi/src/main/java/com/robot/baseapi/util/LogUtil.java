package com.robot.baseapi.util;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.ayit.klog.KLog;

/**
 * Created by lny on 2018/1/10.
 */

public class LogUtil {
    public static void printIntent(Intent intent){
        if (intent!=null && intent.getExtras()!=null){
            JSONObject json = new JSONObject();
            for (String key:intent.getExtras().keySet()){
                json.put(key,intent.getExtras().get(key));
            }
            KLog.json(json.toJSONString());
        }else{
            KLog.d("intent : null");
        }
    }
}

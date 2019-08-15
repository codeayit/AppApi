package com.robot.baseappapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ayit.klog.KLog;
import com.robot.baseapi.base.BaseActivity;
import com.robot.baseapi.db.ConditionBuilder;
import com.robot.baseapi.db.DbUtil;
import com.robot.baseapi.net.NetCallback;
import com.robot.baseapi.net.NetWork;
import com.robot.baseapi.net.NetWorkStringCallBack;
import com.robot.baseapi.net.SignCallback;
import com.robot.baseapi.util.AppUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.MediaType;

public class MainActivity extends BaseActivity {
    String action ="com.robot.videoplayer.TestActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(getContext());
        ConditionBuilder end = ConditionBuilder.getInstance()
                .start()
                .left()
                .addCondition("a", "a")
                .or()
                .addCondition("b","b")
                .right()
                .and()
                .left()
                .addCondition("c","c")
                .or()
                .addCondition("d","d")
                .right()
                .end();
        String[] condition = end.condition();

        StringBuilder sb = new StringBuilder();

        for (String s:condition){
            sb.append(s);
        }
        KLog.d(sb.toString());

    }

    @Override
    public void initData() {

    }

    public void btn2(View view){

//


//        com.alibaba.fastjson.JSONObject data = new com.alibaba.fastjson.JSONObject();
//
//        JSONArray list = new JSONArray();
//        com.alibaba.fastjson.JSONObject jo = new com.alibaba.fastjson.JSONObject();
//        jo.put("boxId", 1);
//        jo.put("count", 1);
//        list.add(jo);
//
//        data.put("deviceId", 1);
//        data.put("accountId", 255);
//        data.put("customer", "吃豪杰");
//        data.put("deviceSN", "lnyserialno");
//        data.put("list", list);
//        KLog.d(JSON.toJSONString(data));

//        OkHttpUtils.postString()
//                .mediaType(MediaType.parse("application/json;charset=UTF-8"))
//                .url("http://192.168.168.185:9090/testPost")
//                .addHeader("sn","lnyserialno1")
//                .content(JSON.toJSONString(data))
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        KLog.d(response);
//                    }
//                });




//        NetWork.getInstance(getContext())
//                .postString()
//                .url("http://192.168.168.185:9090/testPost")
//                .postStringObject(data)
////                .globalCodeFilter(false)
//                .build()
//                .execute(new NetWorkStringCallBack() {
//                    @Override
//                    public void onError(String msg) {
//
//                    }
//                    @Override
//                    public void onResponse(String s) {
//                        t("123");
//                    }
//
//                });
    }

    @Override
    public void initView() {

    }

    public String blankLenght(int lenght){
        StringBuffer buffer = new StringBuffer();
        for (int i=0;i<lenght;i++){
            buffer.append(" ");
        }
        return buffer.toString();
    }


    public void print(String msg){
        Log.d("klog",msg);
    }

    public void btn0(View view) {
        AppUtil.openExteranlActivity(getApplicationContext(), action,Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppUtil.openExteranlActivity(getApplicationContext(), action,Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
        },3*1000);
    }

    public void btn1(View view) {
        AppUtil.openExteranlActivity(getApplicationContext(), action,Intent.FLAG_ACTIVITY_SINGLE_TOP,Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppUtil.openExteranlActivity(getApplicationContext(), action,Intent.FLAG_ACTIVITY_SINGLE_TOP,Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
        },3*1000);
    }
}

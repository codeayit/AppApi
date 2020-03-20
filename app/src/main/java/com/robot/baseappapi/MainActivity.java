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
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import java.util.ArrayList;
import java.util.List;
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

        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(permissions -> {
                    // Storage permission are allowed.  同意
                    KLog.d("权限 ： onGranted");
                    init();
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed. 拒绝
                    KLog.d("权限 ： onDenied");
                    init();
                })
                .start();

    }

    @Override
    public void initData() {
//        DbUtil.clear(TestDb.class);
//
//        List<TestDb> data = new ArrayList<>();
//        data.add(new TestDb(1,1));
//        data.add(new TestDb(2,1));
//        data.add(new TestDb(3,1));
//        data.add(new TestDb(1,2));
//        data.add(new TestDb(2,2));
//        data.add(new TestDb(3,2));
//        DbUtil.save(data);
//
//
//        ConditionBuilder end = ConditionBuilder.getInstance()
//                .isLog()
//                .start()
//                .left()
//                .addCondition("status", "1")
//                .or()
//                .addCondition("status","2")
//                .right()
//                .and()
//                .addCondition("type","1")
//                .end();
//
//        List<TestDb> testDbs = DbUtil.find(TestDb.class, end);
//
//        KLog.json(JSON.toJSONString(testDbs));

        String str = "{\"method\":\"get\",\"url\":\"http:\\\\/\\\\/www.houluzhai.top:9310\\\\/coffeeVendor\\\\/device\\\\/getGoodsLibsStrategyDetailByDeviceId\",\"params\":{\"deviceSn\":\"44:18:08:09:3f:c2\",\"deviceId\":\"80\"},\"headers\":{\"sn\":\"44:18:08:09:3f:c2\"},\"response_success\":{\"message\":\"ok\",\"content\":{\"agent\":42,\"remark\":\"\",\"isDel\":0,\"updated\":\"\",\"strategyCode\":\"STR000066\",\"isWater\":1,\"priceList\":[{\"cupParam\":\"[{\\\"index\\\":0,\\\"value\\\":\\\"大杯\\\",\\\"isShow\\\":true,\\\"waterWeight\\\":\\\"20\\\",\\\"materialWeight\\\":\\\"30\\\",\\\"price\\\":\\\"3\\\"},{\\\"index\\\":1,\\\"value\\\":\\\"中杯\\\",\\\"isShow\\\":false},{\\\"index\\\":2,\\\"value\\\":\\\"小杯\\\",\\\"isShow\\\":false}]\",\"libsCupParam\":\"\",\"cupPrice\":\"\",\"updated\":\"\",\"imageUrl\":\"http:\\\\/\\\\/www.houluzhai.top\\\\/imgs\\\\/1220d907cedf4de8ad3193134592fd70卡布奇诺_coffee.jpg\",\"isHot\":\"1\",\"strategyId\":66,\"id\":270,\"boxCode\":\"1号通道\",\"hotPrice\":0,\"price\":20,\"createTime\":\"2019-12-26 11:07:07\",\"libsId\":102,\"boxId\":272,\"goodsName\":\"卡布奇诺咖啡\",\"created\":\"jakecy\",\"imagePath\":\"\"},{\"cupParam\":\"[{\\\"index\\\":0,\\\"value\\\":\\\"大杯\\\",\\\"isShow\\\":true,\\\"waterWeight\\\":\\\"20\\\",\\\"materialWeight\\\":\\\"10\\\",\\\"price\\\":\\\"10\\\"},{\\\"index\\\":1,\\\"value\\\":\\\"中杯\\\",\\\"isShow\\\":true,\\\"waterWeight\\\":\\\"30\\\",\\\"materialWeight\\\":\\\"20\\\",\\\"price\\\":\\\"5\\\"},{\\\"index\\\":2,\\\"value\\\":\\\"小杯\\\",\\\"isShow\\\":true,\\\"waterWeight\\\":\\\"35\\\",\\\"materialWeight\\\":\\\"30\\\",\\\"price\\\":\\\"0\\\"}]\",\"libsCupParam\":\"\",\"cupPrice\":\"\",\"updated\":\"\",\"imageUrl\":\"http:\\\\/\\\\/www.houluzhai.top\\\\/imgs\\\\/56be7cf503d34ce3bde58db4fa1ded65HarinaCoffee.jpg\",\"isCold\":1,\"isHot\":\"1\",\"strategyId\":66,\"id\":271,\"boxCode\":\"2号通道\",\"hotPrice\":3,\"price\":15,\"createTime\":\"2019-12-26 11:07:07\",\"libsId\":102,\"coldPrice\":0,\"boxId\":273,\"goodsName\":\"瑞幸咖啡\",\"created\":\"jakecy\",\"imagePath\":\"\"},{\"cupParam\":\"[{\\\"index\\\":0,\\\"value\\\":\\\"大杯\\\",\\\"isShow\\\":true,\\\"waterWeight\\\":\\\"20\\\",\\\"materialWeight\\\":\\\"10\\\",\\\"price\\\":\\\"10\\\"},{\\\"index\\\":1,\\\"value\\\":\\\"中杯\\\",\\\"isShow\\\":true,\\\"waterWeight\\\":\\\"30\\\",\\\"materialWeight\\\":\\\"20\\\",\\\"price\\\":\\\"5\\\"},{\\\"index\\\":2,\\\"value\\\":\\\"小杯\\\",\\\"isShow\\\":true,\\\"waterWeight\\\":\\\"35\\\",\\\"materialWeight\\\":\\\"30\\\",\\\"price\\\":\\\"0\\\"}]\",\"libsCupParam\":\"\",\"cupPrice\":\"\",\"updated\":\"\",\"imageUrl\":\"http:\\\\/\\\\/www.houluzhai.top\\\\/imgs\\\\/1e9d2b0fee4a4fdc96436cc017833890焦糖拿铁咖啡.jpg\",\"isHot\":\"1\",\"strategyId\":66,\"id\":272,\"boxCode\":\"3号通道\",\"hotPrice\":0,\"price\":13,\"createTime\":\"2019-12-26 11:07:07\",\"libsId\":102,\"boxId\":274,\"goodsName\":\"焦糖拿铁\",\"created\":\"jakecy\",\"imagePath\":\"\"}],\"companyName\":\"名捕科技有限公司(代理商)\",\"id\":66,\"libsName\":\"\",\"createTime\":\"2019-12-26 11:08:28\",\"libsId\":102,\"companyType\":1,\"strategyName\":\"元旦优惠价\",\"created\":\"jakecy\",\"companyId\":42},\"code\":200,\"success\":true}}";

        KLog.setOnLogListener(new KLog.OnLogListener() {
            @Override
            public void onLog(String log) {
//                Log.d("lis",log);
            }
        });

        for (int i = 0; i < 100; i++) {
            KLog.json(str);
            Log.d("num","--------------------- "+i);
        }
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

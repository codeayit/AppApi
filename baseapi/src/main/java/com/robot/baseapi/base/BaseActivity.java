package com.robot.baseapi.base;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.ayit.crashlibrary.CrashBaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lny on 2017/11/18.
 */

public abstract class BaseActivity extends CrashBaseActivity {


    private static final Map<String, Boolean> activitySate = new HashMap<>();

    public Activity getContext(){
        return this;
    }
    public static boolean isExist(Class<? extends BaseActivity> clazz) {

        String key = clazz.getSimpleName() + "_exist";
        if (activitySate.containsKey(key)) {
            return activitySate.get(key);
        }
        return false;
    }
    public static boolean isActive(Class<? extends BaseActivity> clazz) {
        String key = clazz.getSimpleName() + "_active";
        if (activitySate.containsKey(key)) {
            return activitySate.get(key);
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySate.put(getClass().getSimpleName() + "_exist", true);
    }
    public void init(){
        initData();
        initView();
    }
    public abstract void initData();
    public abstract void initView();

    @Override
    protected void onResume() {
        super.onResume();
        activitySate.put(getClass().getSimpleName() + "_active", true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        activitySate.remove(getClass().getSimpleName() + "_active");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activitySate.remove(getClass().getSimpleName() + "_exist");
    }




    public void t(String msg,int druation){
        if (!TextUtils.isEmpty(msg))
            Toast.makeText(getApplicationContext(),msg,druation).show();
    }

    public void t(String msg){
        t(msg,Toast.LENGTH_SHORT);
    }
    /**
     * 屏幕常亮
     */
    public final void keepScreenLight(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }



}

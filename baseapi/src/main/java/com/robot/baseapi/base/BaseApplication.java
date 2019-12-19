package com.robot.baseapi.base;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ayit.crashlibrary.Crash;
import com.ayit.crashlibrary.CrashHandler;
import com.ayit.klog.KLog;
import com.robot.baseapi.BuildConfig;
import com.robot.baseapi.util.SPManager;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;


/**
 * Created by lny on 2017/10/12.
 */

public class BaseApplication extends MultiDexApplication {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
//        LitePal.initialize(this);
        instance  = this;
//        iniKlog();
//        iniSp();
        initCrash();
    }

    public void initCrash(){
        CrashHandler.getInstance().initUnCrash(this,true);
        CrashHandler.getInstance().setOnCrashCallBack(new CrashHandler.OnCrashCallBack() {
            @Override
            public void onCrash(Crash crash) {
                KLog.e(crash.toString());
            }
        });
    }

    public void iniSp() {
        SPManager.ini(this);
    }

    public void iniKlog() {
        iniKlog("klog");
    }
    public void iniKlog(String tag){
        KLog.init(this,true, tag);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Application getInstance(){
        return instance;
    }

    public void t(String msg,int druation){
        if (!TextUtils.isEmpty(msg))
            Toast.makeText(getApplicationContext(),msg,druation).show();
    }

    public void t(String msg){
        t(msg,Toast.LENGTH_SHORT);
    }
}

package com.robot.baseapi.FileDownloader;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.ayit.klog.KLog;
import com.robot.baseapi.base.BaseService;
import com.robot.baseapi.util.LogUtil;

public class OkDlService extends BaseService {


    private OkDlManager okDlManager;

    public OkDlService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        okDlManager = OkDlManager.getInstance();
        okDlManager.onCreate();
        KLog.d("OkDlService:onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KLog.d("OkDlService:onStartCommand()");
        LogUtil.printIntent(intent);

        Bundle extras = intent.getExtras();

        if (extras!=null&& extras.containsKey("action")){
            String action = intent.getStringExtra("action");
            switch (action){
                case "init":
                    okDlManager.init(intent.getIntExtra("task_count",2),intent.getIntExtra("progress_duration",1000));
                    break;
                case "add":
                    okDlManager.add(
                            intent.getIntExtra("flag",0),
                            intent.getStringExtra("url"),
                            intent.getStringExtra("dir"),
                            intent.getStringExtra("fileName")
                    );
                    break;
                case "cancle":
                    okDlManager.cancle(intent.getStringExtra("url"));
                    break;
                case "cancleAll":
                    okDlManager.cancleAll();
                    break;
                case "pause":
                    okDlManager.pause(intent.getStringExtra("url"));
                    break;
                case "pauseAll":
                    okDlManager.pauseAll();
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        okDlManager.onDestory();
    }
}

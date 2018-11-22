package com.robot.baseapi.FileDownloader;

import android.content.Intent;
import android.os.IBinder;

import com.ayit.klog.KLog;
import com.robot.baseapi.base.BaseService;

public class OkDlService extends BaseService {


    private OkDlManager okDlManager;

    public OkDlService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        okDlManager = OkDlManager.getInstance();
        okDlManager.onCreate();
//        okDlManager.init(this);
        KLog.d("OkDlService:onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KLog.d("OkDlService:onStartCommand()");
        if (intent.getExtras()!=null && intent.getExtras().containsKey("task_count")){
            okDlManager.init(intent.getIntExtra("task_count",2));
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

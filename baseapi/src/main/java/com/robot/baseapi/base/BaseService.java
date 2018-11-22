package com.robot.baseapi.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ayit.crashlibrary.CrashBaseService;

public class BaseService extends CrashBaseService {
    public BaseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

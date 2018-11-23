package com.robot.baseapi.other;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.SoftReference;

/**
 * Created by lny on 2018/3/16.
 */

public class SafeHandler extends Handler {

    private SoftReference<SafeHandlerMsgCallback> callback;

    public void regist(SafeHandlerMsgCallback msgCallback) {
        callback = new SoftReference<SafeHandlerMsgCallback>(msgCallback);
    }

    public void unregist() {
        if (callback != null) {
            removeCallbacksAndMessages();
            callback = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (callback != null && callback.get() != null) {
            callback.get().handleMessage(msg);
        }
    }

    public void removeCallbacksAndMessages() {
        this.removeCallbacksAndMessages(null);
    }
}

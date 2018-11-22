package com.robot.baseapi.FileDownloader;

public interface OkDlListener {
    void onPrepare(OkDlTask task);
    void onStart(OkDlTask task);
    void onProgress(OkDlTask task);
    void onStop(OkDlTask task);
    void onError(OkDlTask task, String error);
    void onFinish(OkDlTask task);
    void onCancle(OkDlTask task);
}

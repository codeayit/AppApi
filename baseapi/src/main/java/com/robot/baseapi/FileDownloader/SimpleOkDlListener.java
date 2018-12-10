package com.robot.baseapi.FileDownloader;

public abstract class SimpleOkDlListener implements OkDlListener {
    @Override
    public void onPrepare(OkDlTask task) {
        onDownloadChanged(task);
    }
    @Override
    public void onStart(OkDlTask task) {
        onDownloadChanged(task);
    }
    @Override
    public void onProgress(OkDlTask task) {
        onDownloadChanged(task);
    }
    @Override
    public void onStop(OkDlTask task) {
        onDownloadChanged(task);
    }
    @Override
    public void onError(OkDlTask task, String error) {
        onErrorMsg(error);
        onDownloadChanged(task);
    }
    @Override
    public void onFinish(OkDlTask task) {
        onDownloadChanged(task);
    }
    @Override
    public void onCancle(OkDlTask task) {
        onDownloadChanged(task);
    }
    public abstract void  onDownloadChanged(OkDlTask task);

    public abstract void onErrorMsg(String error);
}

package com.robot.baseapi.FileDownloader;

import android.support.annotation.NonNull;

import java.util.List;

public interface IOkDlManager {
    List<OkDlTask> getAll();
    List<OkDlTask> getAll(int flag);
    OkDlTask get(@NonNull String url);
    void add(int flag, @NonNull String url, @NonNull String dir);
    void add(int flag, @NonNull String url, @NonNull String dir, @NonNull String fileName);
    void cancle(@NonNull String url);
    void cancleAll();
    void pause(@NonNull String url);
    void pauseAll();



    void onCreate();
    void onDestory();

}

package com.robot.baseapi.FileDownloader;

import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;


import com.ayit.klog.KLog;
import com.robot.baseapi.db.ConditionBuilder;
import com.robot.baseapi.db.DbUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class OkDlManager {

    private static Application mApplication;

    private static int TASK_COUNT = 2;

    private static long PROGRESS_DURATION = 1000;


    private static OkDlManager manager;
    //    private Context mContext;
    private int taskCount;
    private int progressDuration;
    private Map<Integer, OkDlListener> listenerMap;

    private Map<String, OkFileCallBack> callBackMap;


    public OkDlManager() {
        if (callBackMap == null)
            callBackMap = new HashMap<>();
        if (listenerMap == null)
            listenerMap = new HashMap<>();
    }

    public void addDlListener(int flag, OkDlListener dlListener) {
        listenerMap.put(flag, dlListener);
    }


    public OkDlListener getListener(int flag) {
        return listenerMap.get(flag);
    }

    public void removeDlListener(int flag) {
        listenerMap.remove(flag);
    }

    public static OkDlManager getInstance() {
        if (manager == null) {
            synchronized (OkDlManager.class) {
                if (manager == null) {
                    manager = new OkDlManager();
                }
            }
        }
        return manager;
    }

    public List<OkDlTask> getAllTask(){
        return getAll();
    }

    protected List<OkDlTask> getAll() {
        return DbUtil.findAll(OkDlTask.class);
    }

    public List<OkDlTask> getAllTask(int flag){
        return getAll(flag);
    }
    protected List<OkDlTask> getAll(int flag) {
        return DbUtil.find(OkDlTask.class,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.flag, String.valueOf(flag)).end());
    }

    public OkDlTask getTask(String url){
        return get(url);
    }
    protected OkDlTask get(String url) {
        return DbUtil.findFirst(OkDlTask.class, ConditionBuilder.getInstance().start().addCondition(OkDlTask.Field.url, url).end());
    }


    protected void add(int flag, String url, String dir) {
        String[] split = url.split("/");
        String fileName = split[split.length - 1];
        add(flag, url, dir, fileName);
    }
    public void addTask(int flag,String url,String dir){
        String[] split = url.split("/");
        String fileName = split[split.length - 1];
//        add(flag, url, dir, fileName);
       addTask(flag, url, dir, fileName);
    }

    public void addTask(int flag, String url, String dir, String fileName){
        Intent intent = new Intent(mApplication,OkDlService.class);
        intent.putExtra("action","add");
        intent.putExtra("flag",flag);
        intent.putExtra("url",url);
        intent.putExtra("dir",dir);
        intent.putExtra("fileName",fileName);
        mApplication.startService(intent);
    }


    protected void add(int flag, String url, String dir, String fileName) {

        File dirF = new File(dir);
        if (!dirF.exists()) {
            dirF.mkdirs();
        }
        OkDlTask task = DbUtil.findFirst(OkDlTask.class,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.url, url).end());
        if (task == null) {
            task = new OkDlTask(flag, url, dir, fileName);
            DbUtil.save(task);
        } else {
            File file = new File(task.getLocalPath());
            if (!file.exists()) {
                task.setCurrentLength(0);
            }
            updateStatusByUrl(task.getUrl(), OkDlTask.Status.STATUS_WAITING);
        }
        newCall(task);
    }

    public void cancleTask(String url){
        Intent intent = new Intent(mApplication,OkDlService.class);
        intent.putExtra("action","cancle");
        intent.putExtra("url",url);
        mApplication.startService(intent);
    }

    protected void cancle(String url) {
        OkHttpUtils.getInstance().cancelTag(url);
        OkDlTask first = DbUtil.findFirst(OkDlTask.class,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.url, url).end());
        KLog.d("cancle : first _ " + first);
        if (first != null) {
            first.setCurrentLength(0);
            first.setTotalLength(0);

            switch (first.getStatus()) {
                case OkDlTask.Status.STATUS_DOWNLOADING:
                    callBackMap.get(url).setStatus(OkDlTask.Status.STATUS_DISCARD);
                    break;
                case OkDlTask.Status.STATUS_ERROR:
                case OkDlTask.Status.STATUS_PAUSE:
                case OkDlTask.Status.STATUS_WAITING:
                    //存在文件和待执行任务，删除任务，并删除文件
                    if (getListener(first.getFlag()) != null) {
                        first.setStatus(OkDlTask.Status.STATUS_DISCARD);
                        getListener(first.getFlag()).onCancle(first);
                    }
                    int count = DbUtil.delete(OkDlTask.class,
                            ConditionBuilder.getInstance()
                                    .start()
                                    .addCondition(OkDlTask.Field.url, url).end());
                    KLog.d("count : " + count);
                    deleteFile(first);
                    break;
            }
        }
    }

    public void cancleAllTask(){
        Intent intent = new Intent(mApplication,OkDlService.class);
        intent.putExtra("action","cancleAll");
        mApplication.startService(intent);
    }

    protected void cancleAll() {
        List<OkDlTask> all = getAll();
        for (OkDlTask task : all) {
            cancle(task.getUrl());
        }
    }

    public void pauseTask(@NonNull String url){
        Intent intent = new Intent(mApplication,OkDlService.class);
        intent.putExtra("action","pause");
        intent.putExtra("url",url);
        mApplication.startService(intent);
    }
    protected void pause(@NonNull String url) {
        OkDlTask first = DbUtil.findFirst(OkDlTask.class,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.url, url).end());
        if (first != null) {
            switch (first.getStatus()) {
                case OkDlTask.Status.STATUS_DOWNLOADING:
                    callBackMap.get(url).setStatus(OkDlTask.Status.STATUS_PAUSE);
                    break;
                case OkDlTask.Status.STATUS_WAITING:
                    updateStatusByUrl(url, OkDlTask.Status.STATUS_WAITING);
                    break;
            }
        }

    }


    public void pauseAllTask(){
        Intent intent = new Intent(mApplication,OkDlService.class);
        intent.putExtra("action","pauseAll");
        mApplication.startService(intent);
    }

    protected void pauseAll() {
        List<OkDlTask> waittingTasks = DbUtil.find(OkDlTask.class,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.status, String.valueOf(OkDlTask.Status.STATUS_WAITING)).end());
        for (OkDlTask task : waittingTasks) {
            OkDlManager.getInstance().pause(task.getUrl());
        }


        List<OkDlTask> dlingTasks = DbUtil.find(OkDlTask.class,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.status, String.valueOf(OkDlTask.Status.STATUS_DOWNLOADING)).end());
        for (OkDlTask task : dlingTasks) {
            OkDlManager.getInstance().pause(task.getUrl());
        }
    }

    private void deleteFile(OkDlTask task) {
        File file = new File(task.getLocalPath());
        if (file.exists()) {
            file.delete();
        }
    }


    protected void init(int parallelTaskCount,long progressDuration) {
        this.TASK_COUNT = parallelTaskCount;
        this.PROGRESS_DURATION = progressDuration;
        this.taskCount = 0;
    }

    /**
     * @param application
     */
    public static void init(@NonNull Application application) {
        init(application, TASK_COUNT,PROGRESS_DURATION);
    }

    /**
     * @param application
     * @param parallelTaskCount
     */
    public static void init(@NonNull Application application, int parallelTaskCount,long progressDuration) {
        mApplication = application;
        Intent serviceIntent = new Intent(application, OkDlService.class);
        serviceIntent.putExtra("action","init");
        serviceIntent.putExtra("task_count", parallelTaskCount);
        serviceIntent.putExtra("progress_duration",progressDuration);
        application.startService(serviceIntent);
    }

    protected void onCreate() {
        getInstance();
        //处理上次遗留的下载任务,重置为 停止状态
        ContentValues values = new ContentValues();
        values.put(OkDlTask.Field.status, OkDlTask.Status.STATUS_PAUSE);
        DbUtil.update(OkDlTask.class,
                values,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.status, String.valueOf(OkDlTask.Status.STATUS_WAITING))
                        .or()
                        .addCondition(OkDlTask.Field.status, String.valueOf(OkDlTask.Status.STATUS_DOWNLOADING))
                        .end());
    }

    private void updateStatusByUrl(String url, int status) {
        ContentValues values = new ContentValues();
        values.put(OkDlTask.Field.status, status);
        DbUtil.update(OkDlTask.class,
                values,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.url, url)
                        .end());
    }

    private void deleteByUrl(String url) {
        DbUtil.delete(OkDlTask.class,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.url, url)
                        .end());
    }


    private void newCall(final OkDlTask task) {

        if (getListener(task.getFlag()) != null) {
            task.setStatus(OkDlTask.Status.STATUS_WAITING);
            ContentValues values = new ContentValues();
            values.put(OkDlTask.Field.status, task.getStatus());
            DbUtil.update(OkDlTask.class, values,
                    ConditionBuilder.getInstance()
                            .start()
                            .addCondition(OkDlTask.Field.url, task.getUrl()).end());
            getListener(task.getFlag()).onPrepare(task);
        }
        if (taskCount >= TASK_COUNT) {
            return;
        }
        taskCount++;
        if (getListener(task.getFlag()) != null) {
            task.setStatus(OkDlTask.Status.STATUS_DOWNLOADING);
            ContentValues values = new ContentValues();
            values.put(OkDlTask.Field.status, task.getStatus());
            DbUtil.update(OkDlTask.class, values,
                    ConditionBuilder.getInstance()
                            .start()
                            .addCondition(OkDlTask.Field.url, task.getUrl()).end());
            getListener(task.getFlag()).onPrepare(task);
        }
        KLog.d("RANGE" + " : " + "bytes=" + task.getCurrentLength() + "-" + task.getUrl());
        OkFileCallBack callback = null;
        OkHttpUtils.get()
                .url(task.getUrl())
                .tag(task.getUrl())
                .addHeader("RANGE", "bytes=" + task.getCurrentLength() + "-")
                .build()
                .execute(
//                        String destFileDir, String destFileName
                        callback = new OkFileCallBack(task.getDir(), task.getFileName(), task.getCurrentLength()) {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                if (this.isCancle()) {
                                    switch (this.getStatus()) {
                                        case OkDlTask.Status.STATUS_PAUSE:
                                            task.setStatus(OkDlTask.Status.STATUS_PAUSE);
                                            if (getListener(task.getFlag()) != null) {
                                                getListener(task.getFlag()).onStop(task);
                                            }
                                            updateStatusByUrl(task.getUrl(), task.getStatus());
                                            break;
                                        case OkDlTask.Status.STATUS_DISCARD:
                                            task.setStatus(OkDlTask.Status.STATUS_DISCARD);
                                            task.setTotalLength(0);
                                            task.setCurrentLength(0);
                                            if (getListener(task.getFlag()) != null) {
                                                getListener(task.getFlag()).onCancle(task);
                                            }
                                            deleteByUrl(task.getUrl());
                                            break;
                                    }
                                } else {
                                    if (getListener(task.getFlag()) != null) {
                                        task.setStatus(OkDlTask.Status.STATUS_ERROR);
                                        updateStatusByUrl(task.getUrl(), task.getStatus());
                                        getListener(task.getFlag()).onError(task, e.getMessage());
                                    }
                                }

                            }

                            @Override
                            public void onResponse(File response, int id) {
                                if (getListener(task.getFlag()) != null) {
                                    if (this.isCancle()) {
                                        switch (this.getStatus()) {
                                            case OkDlTask.Status.STATUS_PAUSE:
                                                task.setStatus(OkDlTask.Status.STATUS_PAUSE);
                                                if (getListener(task.getFlag()) != null) {
                                                    getListener(task.getFlag()).onStop(task);
                                                }
                                                updateStatusByUrl(task.getUrl(), task.getStatus());
                                                break;
                                            case OkDlTask.Status.STATUS_DISCARD:
                                                task.setStatus(OkDlTask.Status.STATUS_DISCARD);
                                                task.setTotalLength(0);
                                                task.setCurrentLength(0);
                                                if (getListener(task.getFlag()) != null) {
                                                    getListener(task.getFlag()).onCancle(task);
                                                }
                                                deleteByUrl(task.getUrl());
                                                break;
                                        }
                                    } else {
                                        task.setStatus(OkDlTask.Status.STATUS_SUCCESS);
                                        if (getListener(task.getFlag()) != null) {
                                            getListener(task.getFlag()).onFinish(task);
                                        }
                                        deleteByUrl(task.getUrl());
                                    }
                                }
                            }


                            @Override
                            public void inProgressSubThread(long currentLenght, long totalLength) {
                                task.setCurrentLength(currentLenght);
                                ContentValues values = new ContentValues();
                                values.put(OkDlTask.Field.currentLength, task.getCurrentLength());
//                                values.put(OkDlTask.Field.totalLength,totalLength);
                                DbUtil.update(OkDlTask.class, values,
                                        ConditionBuilder.getInstance()
                                                .start()
                                                .addCondition(OkDlTask.Field.url, task.getUrl()).end());
                            }

                            @Override
                            public void onStart(long totalLength) {
                                task.setTotalLength(totalLength);
                                ContentValues values = new ContentValues();
//                                values.put(OkDlTask.Field.currentLength,currentLenght);
                                values.put(OkDlTask.Field.totalLength, task.getTotalLength());

                                DbUtil.update(OkDlTask.class, values,
                                        ConditionBuilder.getInstance()
                                                .start()
                                                .addCondition(OkDlTask.Field.url, task.getUrl()).end());
                                if (getListener(task.getFlag()) != null) {
                                    getListener(task.getFlag()).onPrepare(task);
                                }
                            }

                            @Override
                            public void inProgress(float progress, long total, int id) {
                                super.inProgress(progress, total, id);
                                if (getListener(task.getFlag()) != null) {
                                    getListener(task.getFlag()).onProgress(task);
                                }
                            }

                            @Override
                            public void onAfter(int id) {
                                super.onAfter(id);
                                taskCount--;
                                callBackMap.remove(task.getUrl());
                                OkDlTask first = DbUtil.findFirst(OkDlTask.class,
                                        ConditionBuilder.getInstance()
                                                .start()
                                                .addCondition(OkDlTask.Field.status, String.valueOf(OkDlTask.Status.STATUS_WAITING)).end());
                                if (first != null) {
                                    add(first.getFlag(), first.getUrl(), first.getDir(), first.getFileName());
                                }

                            }
                        }
                );
        callback.setProgressDuration(progressDuration);
        callBackMap.put(task.getUrl(), callback);
    }


    public void onDestory() {
        pauseAll();
        OkHttpUtils.getInstance().cancelTag(null);
    }


}

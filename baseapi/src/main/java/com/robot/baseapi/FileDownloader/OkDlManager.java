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
import java.util.ArrayList;
import java.util.Calendar;
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
    private Map<Integer, OkDlListener> listenerMap;

    private List<OkDlListener> listeners;

    private Map<String, OkFileCallBack> callBackMap;


    public OkDlManager() {
        if (callBackMap == null) callBackMap = new HashMap<>();
        if (listenerMap == null) listenerMap = new HashMap<>();
        if (listeners == null) listeners = new ArrayList<>();
    }

    public void addDlListener(int flag, OkDlListener dlListener) {
        listenerMap.put(flag, dlListener);
    }

    public void addDlListener(OkDlListener dlListener) {
        if (dlListener != null)
            listeners.add(dlListener);
    }

    public void removeDlListener(OkDlListener dlListener) {
        if (listeners.contains(dlListener)) {
            listeners.remove(dlListener);
        }
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

    public List<OkDlTask> getAllTask() {
        return getAll();
    }

    protected List<OkDlTask> getAll() {
        return DbUtil.findAll(OkDlTask.class);
    }

    public List<OkDlTask> getAllTask(int flag) {
        return getAll(flag);
    }

    protected List<OkDlTask> getAll(int flag) {
        return DbUtil.find(OkDlTask.class,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.flag, String.valueOf(flag)).end());
    }

    public OkDlTask getTask(String url) {
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

    public void addTask(int flag, String url, String dir) {
        String[] split = url.split("/");
        String fileName = split[split.length - 1];
//        add(flag, url, dir, fileName);
        addTask(flag, url, dir, fileName);
    }

    public void addTask(int flag, String url, String dir, String fileName) {
        if (mApplication!=null){
            Intent intent = new Intent(mApplication, OkDlService.class);
            intent.putExtra("action", "add");
            intent.putExtra("flag", flag);
            intent.putExtra("url", url);
            intent.putExtra("dir", dir);
            intent.putExtra("fileName", fileName);
            mApplication.startService(intent);
        }

    }

    public boolean resumeTask(String url) {
        OkDlTask task = DbUtil.findFirst(OkDlTask.class,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.url, url).end());
        if (task != null && (task.getStatus() == OkDlTask.Status.STATUS_ERROR || task.getStatus() == OkDlTask.Status.STATUS_PAUSE)) {
            newCall(task);
            return true;
        } else {
            return false;
        }
    }

    public void resumeAllTask(int flag) {
        List<OkDlTask> list = DbUtil.find(OkDlTask.class,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.status, String.valueOf(OkDlTask.Status.STATUS_PAUSE))
                        .or()
                        .addCondition(OkDlTask.Field.status, String.valueOf(OkDlTask.Status.STATUS_ERROR))
                        .and()
                        .addCondition(OkDlTask.Field.flag, String.valueOf(flag))
                        .end());
        for (OkDlTask task : list) {
            resumeTask(task.getUrl());
        }
    }

    public void resumeAllTask() {
        List<OkDlTask> list = DbUtil.find(OkDlTask.class,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.status, String.valueOf(OkDlTask.Status.STATUS_PAUSE))
                        .or()
                        .addCondition(OkDlTask.Field.status, String.valueOf(OkDlTask.Status.STATUS_ERROR))
                        .end());
        for (OkDlTask task : list) {
            resumeTask(task.getUrl());
        }
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
            newCall(task);
        } else {
            if (task.getStatus() == OkDlTask.Status.STATUS_DOWNLOADING && taskCount != 0) {
                return;
            }
            File file = new File(task.getLocalPath());
            if (!file.exists()) {
                task.setCurrentLength(0);
                updateStatusByUrl(task.getUrl(), OkDlTask.Status.STATUS_WAITING);
                newCall(task);
            } else {
                if (task.getTotalLength() == task.getCurrentLength() && (task.getTotalLength() != 0)) {
                    task.setStatus(OkDlTask.Status.STATUS_SUCCESS);
                    if (getListener(task.getFlag()) != null) {
                        try {
                            getListener(task.getFlag()).onFinish(task);
                        } catch (Exception e) {
                        }
                    }

                    if (listeners != null && !listeners.isEmpty()) {
                        for (OkDlListener listener : listeners) {
                            try {
                                listener.onFinish(task);
                            } catch (Exception e) {
                            }
                        }
                    }

                    deleteByUrl(task.getUrl());
                } else {
                    newCall(task);
                }
            }
        }

    }

    public void cancleTask(String url) {
//        Intent intent = new Intent(mApplication, OkDlService.class);
//        intent.putExtra("action", "cancle");
//        intent.putExtra("url", url);
//        mApplication.startService(intent);
        cancle(url);
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
                    if (taskCount == 0) {
                        if (getListener(first.getFlag()) != null) {
                            first.setStatus(OkDlTask.Status.STATUS_DISCARD);
                            getListener(first.getFlag()).onCancle(first);
                        }
                        if (listeners != null && !listeners.isEmpty()) {
                            for (OkDlListener listener : listeners) {
                                try {
                                    listener.onCancle(first);
                                } catch (Exception e) {
                                }
                            }
                        }
                        int count = DbUtil.delete(OkDlTask.class,
                                ConditionBuilder.getInstance()
                                        .start()
                                        .addCondition(OkDlTask.Field.url, url).end());
                        deleteFile(first);
                    } else {
                        callBackMap.get(url).setStatus(OkDlTask.Status.STATUS_DISCARD);
                    }
                    break;
                case OkDlTask.Status.STATUS_ERROR:
                case OkDlTask.Status.STATUS_PAUSE:
                case OkDlTask.Status.STATUS_WAITING:
                    //存在文件和待执行任务，删除任务，并删除文件
                    if (getListener(first.getFlag()) != null) {
                        first.setStatus(OkDlTask.Status.STATUS_DISCARD);
                        try {
                            getListener(first.getFlag()).onCancle(first);
                        } catch (Exception e) {
                        }
                    }

                    if (listeners != null && !listeners.isEmpty()) {
                        for (OkDlListener listener : listeners) {
                            try {
                                listener.onCancle(first);
                            } catch (Exception e) {
                            }
                        }
                    }

                    int count = DbUtil.delete(OkDlTask.class,
                            ConditionBuilder.getInstance()
                                    .start()
                                    .addCondition(OkDlTask.Field.url, url).end());
                    KLog.d("delete_count:" + count);
                    deleteFile(first);
                    break;
            }
        }
    }

    public void cancleAllTask() {
//        Intent intent = new Intent(mApplication, OkDlService.class);
//        intent.putExtra("action", "cancleAll");
//        mApplication.startService(intent);
        cancleAll();
    }

    protected void cancleAll() {
        List<OkDlTask> all = getAll();
        for (OkDlTask task : all) {
            cancle(task.getUrl());
        }
    }

    protected void cancleAll(int flag) {
        List<OkDlTask> all = getAll(flag);
        for (OkDlTask task : all) {
            cancle(task.getUrl());
        }
    }

    public void cancleAllTask(int flag) {
        cancleAll(flag);
    }

    public void pauseTask(@NonNull String url) {
        if (mApplication!=null) {
            Intent intent = new Intent(mApplication, OkDlService.class);
            intent.putExtra("action", "pause");
            intent.putExtra("url", url);
            mApplication.startService(intent);
        }
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


    public void pauseAllTask() {
        if (mApplication!=null) {
            Intent intent = new Intent(mApplication, OkDlService.class);
            intent.putExtra("action", "pauseAll");
            mApplication.startService(intent);
        }
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


    protected void init(int parallelTaskCount, long progressDuration) {
        this.TASK_COUNT = parallelTaskCount;
        this.PROGRESS_DURATION = progressDuration;
        this.taskCount = 0;
    }

    /**
     * @param application
     */
    public static void init(@NonNull Application application) {
        init(application, TASK_COUNT, PROGRESS_DURATION);
    }

    /**
     * @param application
     * @param parallelTaskCount
     */
    public static void init(@NonNull Application application, int parallelTaskCount, long progressDuration) {
        KLog.d("static init: " + parallelTaskCount + " : " + progressDuration);
        mApplication = application;
        if (mApplication!=null) {
            Intent serviceIntent = new Intent(application, OkDlService.class);
            serviceIntent.putExtra("action", "init");
            serviceIntent.putExtra("task_count", parallelTaskCount);
            serviceIntent.putExtra("progress_duration", progressDuration);
            application.startService(serviceIntent);
        }
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

        task.setStatus(OkDlTask.Status.STATUS_WAITING);
        if (getListener(task.getFlag()) != null) {
            ContentValues values = new ContentValues();
            values.put(OkDlTask.Field.status, task.getStatus());
            DbUtil.update(OkDlTask.class, values,
                    ConditionBuilder.getInstance()
                            .start()
                            .addCondition(OkDlTask.Field.url, task.getUrl()).end());
            try {
                getListener(task.getFlag()).onPrepare(task);
            } catch (Exception e) {
            }
        }
        if (listeners != null && !listeners.isEmpty()) {
            for (OkDlListener listener : listeners) {
                try {
                    listener.onPrepare(task);
                } catch (Exception e) {
                }
            }
        }
        if (taskCount >= TASK_COUNT) {
            return;
        }
        taskCount++;
        task.setStatus(OkDlTask.Status.STATUS_DOWNLOADING);
        ContentValues values = new ContentValues();
        values.put(OkDlTask.Field.status, task.getStatus());
        DbUtil.update(OkDlTask.class, values,
                ConditionBuilder.getInstance()
                        .start()
                        .addCondition(OkDlTask.Field.url, task.getUrl()).end());
        if (getListener(task.getFlag()) != null) {
            try {
                getListener(task.getFlag()).onStart(task);
            } catch (Exception e) {
            }
        }



        if (listeners != null && !listeners.isEmpty()) {
            for (OkDlListener listener : listeners) {
                try {
                    listener.onStart(task);
                } catch (Exception e) {
                }
            }
        }
        KLog.d("RANGE" + " : " + "bytes=" + task.getCurrentLength() + "-" + task.getUrl() +" "+task.getLocalPath());

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
                                                try {
                                                    getListener(task.getFlag()).onStop(task);
                                                } catch (Exception e1) {
                                                }
                                            }
                                            if (listeners != null && !listeners.isEmpty()) {
                                                for (OkDlListener listener : listeners) {
                                                    try {
                                                        listener.onStop(task);
                                                    } catch (Exception e1) {
                                                    }
                                                }
                                            }
                                            updateStatusByUrl(task.getUrl(), task.getStatus());
                                            break;
                                        case OkDlTask.Status.STATUS_DISCARD:
                                            task.setStatus(OkDlTask.Status.STATUS_DISCARD);
                                            task.setTotalLength(0);
                                            task.setCurrentLength(0);
                                            if (getListener(task.getFlag()) != null) {
                                                try {
                                                    getListener(task.getFlag()).onCancle(task);
                                                } catch (Exception e1) {
                                                }
                                            }
                                            if (listeners != null && !listeners.isEmpty()) {
                                                for (OkDlListener listener : listeners) {
                                                    try {
                                                        listener.onCancle(task);
                                                    } catch (Exception e1) {
                                                    }
                                                }
                                            }
                                            deleteByUrl(task.getUrl());
                                            break;
                                    }
                                } else {
                                    task.setStatus(OkDlTask.Status.STATUS_ERROR);
                                    updateStatusByUrl(task.getUrl(), task.getStatus());
                                    if (getListener(task.getFlag()) != null) {
                                        try {
                                            getListener(task.getFlag()).onError(task, e.getMessage());
                                        } catch (Exception e1) {
                                        }
                                    }
                                    if (listeners != null && !listeners.isEmpty()) {
                                        for (OkDlListener listener : listeners) {
                                            try {
                                                listener.onError(task, e.getMessage());
                                            } catch (Exception e1) {
                                            }
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onResponse(File response, int id) {
//                                if (getListener(task.getFlag()) != null) {
                                if (this.isCancle()) {
                                    switch (this.getStatus()) {
                                        case OkDlTask.Status.STATUS_PAUSE:
                                            task.setStatus(OkDlTask.Status.STATUS_PAUSE);
                                            if (getListener(task.getFlag()) != null) {
                                                try {
                                                    getListener(task.getFlag()).onStop(task);
                                                } catch (Exception e) {
                                                }
                                            }
                                            if (listeners != null && !listeners.isEmpty()) {
                                                for (OkDlListener listener : listeners) {
                                                    try {
                                                        listener.onStop(task);
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            }
                                            updateStatusByUrl(task.getUrl(), task.getStatus());
                                            break;
                                        case OkDlTask.Status.STATUS_DISCARD:
                                            task.setStatus(OkDlTask.Status.STATUS_DISCARD);
                                            task.setTotalLength(0);
                                            task.setCurrentLength(0);
                                            if (getListener(task.getFlag()) != null) {
                                                try {
                                                    getListener(task.getFlag()).onCancle(task);
                                                } catch (Exception e) {
                                                }
                                                ;
                                            }
                                            if (listeners != null && !listeners.isEmpty()) {
                                                for (OkDlListener listener : listeners) {
                                                    try {
                                                        listener.onCancle(task);
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            }
                                            deleteByUrl(task.getUrl());
                                            break;
                                    }
                                } else {
                                    task.setStatus(OkDlTask.Status.STATUS_SUCCESS);
                                    if (getListener(task.getFlag()) != null) {
                                        try {
                                            getListener(task.getFlag()).onFinish(task);
                                        } catch (Exception e) {
                                        }
                                    }
                                    if (listeners != null && !listeners.isEmpty()) {
                                        for (OkDlListener listener : listeners) {
                                            try {
                                                listener.onFinish(task);
                                            } catch (Exception e) {
                                            }
                                        }
                                    }
                                    deleteByUrl(task.getUrl());
                                }
                            }
//                            }


                            @Override
                            public void inProgressSubThread(long currentLenght, long totalLength) {
                                task.setCurrentLength(currentLenght);
                                ContentValues values = new ContentValues();
                                values.put(OkDlTask.Field.currentLength, task.getCurrentLength());
                                values.put(OkDlTask.Field.status,OkDlTask.Status.STATUS_DOWNLOADING);
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
                                    try {
                                        getListener(task.getFlag()).onProgress(task);
                                    } catch (Exception e) {
                                    }
                                }

                                if (listeners != null && !listeners.isEmpty()) {
                                    for (OkDlListener listener : listeners) {
                                        try {
                                            listener.onProgress(task);
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            }

                            @Override
                            public void inProgress(float progress, long total, int id) {
                                super.inProgress(progress, total, id);
                                if (getListener(task.getFlag()) != null) {
                                    try {
                                        getListener(task.getFlag()).onProgress(task);
                                    } catch (Exception e) {
                                    }
                                }
                                if (listeners != null && !listeners.isEmpty()) {
                                    for (OkDlListener listener : listeners) {
                                        try {
                                            listener.onProgress(task);
                                        } catch (Exception e) {
                                        }
                                    }
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
        callback.setProgressDuration(PROGRESS_DURATION);
        callBackMap.put(task.getUrl(), callback);
    }


    public void onDestory() {
        pauseAll();
        OkHttpUtils.getInstance().cancelTag(null);
    }


}

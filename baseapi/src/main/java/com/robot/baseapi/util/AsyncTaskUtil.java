package com.robot.baseapi.util;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 运行异步任务
 */
public class AsyncTaskUtil {
    /**
     * 带放回结果到主线程中
     * @param task
     * @param <T>
     */
    public static<T> void run(Task<T> task){
        final Task t = task;
        if (task!=null){
            t.before();
            Observable.just(1).map(
                    new Function<Integer, T>() {
                        @Override
                        public T apply(Integer value) throws Exception {
                           return (T)t.taskRun();
                        }
                    })

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<T>() {
                        @Override
                        public void accept(T value) throws Exception {
                            t.success(value);
                        }
                    });
        }
    }

    public static void run(Task2 task){
        final Task2 t = task;
        if (task!=null){
            t.before();
            Observable.just(1).map(
                    new Function<Integer, Integer>() {
                        @Override
                        public Integer apply(Integer value) throws Exception {
                           t.taskRun();
                            return 1;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer value) throws Exception {
                            t.success();
                        }
                    });
        }
    }






    public interface Task<T> {
        void before();
        T taskRun();
        void success(T t);
    }

    public static abstract class Task2 {
        public void before(){};
        public abstract void taskRun();
        public void success(){};
    }

}

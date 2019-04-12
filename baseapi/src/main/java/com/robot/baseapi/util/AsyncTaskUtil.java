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

    /**
     * 么有返回结果
     * @param task
     */
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


    /**
     *
     * @param task
     * @param <T> 返回结果
     */
    public static<T> void run(Task3<T> task){
        final Task3<T> t = task;
        if (task!=null){
            t.before(t.getParam());
            Observable.just(t.getParam()).map(
                    new Function<T, T>() {
                        @Override
                        public T apply(T value) throws Exception {
                             t.taskRun(value);
                             return value;
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


    /**
     *
     * @param task
     * @param <P> 参数
     * @param <T> 返回结果
     */
    public static<P,T> void run(Task4<P,T> task){
        final Task4<P,T> t = task;
        if (task!=null){
            t.before(t.getParam());
            Observable.just(t.getParam()).map(
                    new Function<P, T>() {
                        @Override
                        public T apply(P value) throws Exception {
                            return t.taskRun(value);
                        }
                    })

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<T>() {
                        @Override
                        public void accept(T value) throws Exception {
                            t.success(t.getParam(),value);
                        }
                    });
        }
    }

    /**
     *
     * @param task
     * @param <P> 参数
     * @param <T> 返回结果
     */
    public static<P,T> void run(Task5<P,T> task){
        final Task5<P,T> t = task;
        if (task!=null){
            t.before(t.getParams());
            Observable.just(t.getParams()).map(
                    new Function<P[], T>() {
                        @Override
                        public T apply(P[] value) throws Exception {
                            return t.taskRun(value);
                        }
                    })

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<T>() {
                        @Override
                        public void accept(T value) throws Exception {
                            t.success(t.getParams(),value);
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
        public Task2() {
        }

        public void before(){};
        public abstract void taskRun();
        public void success(){};
    }

    public static abstract class Task3<P> {
        private P param;

        public Task3(P param) {
            this.param = param;
        }

        public P getParam() {
            return param;
        }

        public abstract void before(P param);
        public abstract void taskRun( P param);
        public abstract void success(P param);
    }

    public static abstract class Task4<P,T> {
        private P param;

        public Task4(P param) {
            this.param = param;
        }

        public P getParam() {
            return param;
        }

        public abstract void before(P param);
        public abstract T taskRun( P param);
        public abstract void success(P param,T t);
    }


    public static abstract class Task5<P,T> {
        private P[] params;

        public Task5(P... params) {
            this.params = params;
        }

        public P[] getParams() {
            return params;
        }

        public abstract void before(P[] params);
        public abstract T taskRun( P[] params);
        public abstract void success(P[] params,T t);
    }


}

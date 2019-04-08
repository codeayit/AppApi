package com.robot.baseapi.net;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ayit.klog.KLog;
import com.ayti.loadinglayout.BaseLoadingLayout;
import com.ayti.loadinglayout.OnPageContentClickListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

/**
 * Created by lny on 2017/10/16.
 */

public class NetWork {
    public static NetWork instance;
    public String mUrl;
    public HashMap<String, String> mParams;
    public HashMap<String, String> mHeaders;
    public Object mTag;
    public Dialog mDialog;
    public boolean isLog;
    public boolean isPost;
    public PostType postType;
    public boolean isLogJson;
    public boolean isPublicParms;
    public boolean isPublicHeaders;
    public boolean isCheckNet;
    public boolean isSign;
    public boolean isGlobalFilter;
    private JSONObject logInfo;
    public BaseLoadingLayout mLoadingLayout;
    public NetWorkStringCallBack mNetWorkStringCallBack;
    public MediaType mContentType;

    public NetWorkExecute netWorkExecute;

    public static Map<String, String> mPublicParams;
    public static Map<String, String> mPublicHeaders;

    public static SignCallback globalSignCallback;
    public SignCallback signCallback;

    private static NetCallback globalNetCallback;
    private static GlobalFilter globalFilter;
    private NetCallback netCallback;


    public static void setGlobalNetCallback(NetCallback globalNetCallback) {
        NetWork.globalNetCallback = globalNetCallback;
    }

    public static void setGlobalFilter(GlobalFilter globalFilter) {
        NetWork.globalFilter = globalFilter;
    }

    /**
     * 设置公共参数
     *
     * @param params
     */
    public static void setPublicParams(Map<String, String> params) {
        mPublicParams = params;
    }

    public static void setPublicHeaders(Map<String, String> headers) {
        mPublicHeaders = headers;
    }

    @Deprecated
    public static NetWork getInstance(Context context) {
        return new NetWork(context);
    }

    public static NetWork getInstance(Object mTag) {
        return new NetWork(mTag);
    }

    public static NetWork getInstance() {
        return new NetWork();
    }

    public NetWork() {
        init();
    }

    public NetWork(Object tag) {
        mTag = tag;
        init();
    }

//    @Deprecated
//    public NetWork(Context context) {
//        mContext = context;
//        mTag = context;
//        init();
//    }

    private void init() {
        mParams = new HashMap<String, String>();
        mHeaders = new HashMap<String, String>();
        isLog = true;
        isLogJson = true;
        isPublicParms = true;
        isPublicHeaders = true;
        isGlobalFilter = true;
        logInfo = new JSONObject(new LinkedHashMap<String, Object>());
        isPost = true;
        isCheckNet = false;
    }


    /**
     * post 请求
     *
     * @return
     */
    public NetWorkUrl post() {
        postType = PostType.form;
        isPost = true;
        logInfo.put("method", "post");
//        mContentType = MediaType.parse("application/json");
        return new NetWorkUrl();
    }

    public NetWorkUrl postForm() {
        return post();
    }

    public NetWorkUrl postString() {
        postType = PostType.json;
        isPost = true;
        logInfo.put("method", "postString");
        mContentType = MediaType.parse("application/json;charset=UTF-8");
        return new NetWorkUrl();
    }

    /**
     * post 请求
     *
     * @return
     */
    public NetWorkUrl get() {
        isPost = false;
        logInfo.put("method", "get");
        return new NetWorkUrl();
    }

    public class NetWorkUrl {
        /**
         * 设置url
         *
         * @param url
         * @return
         */
        public NetWorkParamsTagBulider url(String url) {
            mUrl = url;
            return new NetWorkParamsTagBulider();
        }
    }

    public class NetWorkParamsTagBulider {

        public NetWorkParamsTagBulider contentType(String contentType) {
            mContentType = MediaType.parse(contentType);
            return this;
        }

        public NetWorkParamsTagBulider addParam(String key, Object value) {
            mParams.put(key, value.toString());
            return this;
        }

        public NetWorkParamsTagBulider postStringContent(String json) {
            mParams.put("postString", json);
            return this;
        }

        public NetWorkParamsTagBulider postStringObject(Object object) {
            mParams.put("postString", JSON.toJSONString(object));
            return this;
        }


        public NetWorkParamsTagBulider addHeader(String key, Object value) {
            mHeaders.put(key, value.toString());
            return this;
        }

        /**
         * 添加多个参数
         *
         * @param params
         * @return
         */
        public NetWorkParamsTagBulider params(Map<String, String> params) {
            mParams.putAll(params);
            return this;
        }

        public NetWorkParamsTagBulider headers(Map<String, String> headers) {
            mHeaders.putAll(headers);
            return this;
        }

        /**
         * 设置请求的tag
         *
         * @param tag
         * @return
         */
        public NetWorkParamsTagBulider Tag(Object tag) {
            mTag = tag;
            return this;
        }

        /**
         * 设置dialog
         *
         * @param dialog
         * @return
         */
        public NetWorkParamsTagBulider dialog(Dialog dialog) {
            mDialog = dialog;
            return this;
        }

        /**
         * 是否携带公共参数
         *
         * @param publicParms
         * @return
         */
        public NetWorkParamsTagBulider publicParms(boolean publicParms) {
            isPublicParms = publicParms;
            return this;
        }

        /**
         * 是否携带公共头
         *
         * @param publicHeaders
         * @return
         */
        public NetWorkParamsTagBulider publicHeaders(boolean publicHeaders) {
            isPublicHeaders = publicHeaders;
            return this;
        }


        /**
         * 设置loadinglayout
         *
         * @param loadingLayout
         * @return
         */
        public NetWorkParamsTagBulider loadingLayout(BaseLoadingLayout loadingLayout) {
            mLoadingLayout = loadingLayout;
            mLoadingLayout.setOnPageContentClickListener(new OnPageContentClickListener() {
                @Override
                public void onClick(View view) {
                    new NetWorkExecute().execute(mNetWorkStringCallBack);
                }
            });
            return this;
        }

        /**
         * 是否打印log 默认log true
         *
         * @param log
         * @return
         */
        public NetWorkParamsTagBulider isLog(boolean log) {
            isLog = log;
            return this;
        }

        /**
         * 是否进行签名
         *
         * @param sign
         * @return
         */
        public NetWorkParamsTagBulider isSign(boolean sign) {
            isSign = sign;
            return this;
        }


        /**
         * log 是否以json打印
         *
         * @param json
         * @return
         */
        public NetWorkParamsTagBulider isLogJson(boolean json) {
            isLogJson = json;
            return this;
        }


        /**
         * 单次签名 callback
         *
         * @param callback
         * @return
         */
        public NetWorkParamsTagBulider sign(SignCallback callback) {
            if (callback != null) {
                isSign = true;
            }
            signCallback = callback;
            return this;
        }

        /**
         * 全局code 过滤
         *
         * @param gobalCodeFilter
         * @return
         */
        public NetWorkParamsTagBulider globalCodeFilter(boolean gobalCodeFilter) {
            NetWork.this.isGlobalFilter = gobalCodeFilter;
            return this;
        }

//        public NetWorkParamsTagBulider isCheckNet(boolean checkNet) {
//            isCheckNet = checkNet;
//            return this;
//        }

        public NetWorkParamsTagBulider checkNet(NetCallback netCallback) {
            if (netCallback != null) {
                isCheckNet = true;
            }
            NetWork.this.netCallback = netCallback;
            return this;
        }

        /**
         * 所有参数已经设置完毕 准备开始执行网络
         *
         * @return
         */
        public NetWorkExecute build() {
            return new NetWorkExecute();
        }
    }


    /**
     * 执行网络操作
     */
    public class NetWorkExecute {
        public void execute(final NetWorkStringCallBack callBack) {
            mNetWorkStringCallBack = callBack;


            if (isCheckNet) {
                //判断是否链接wifi
//                if (!NetworkUtil.isWifiConnected(getContext()) && !NetworkUtil.isMobileDataEnable(getContext()) && !NetworkUtil.isInternet(getContext())) {
//                    if (netCallback != null) {
//                        netCallback.onWifiDisable();
//                    } else if (globalNetCallback != null) {
//                        globalNetCallback.onWifiDisable();
//                    }
//                    return;
//                }

            }

            if (isPublicParms) {
                if (mPublicParams != null && !mPublicParams.isEmpty()) {
//                    for (String key : mPublicParams.keySet()) {
//                        mParams.put(key, mPublicParams.get(key));
//                    }
                    mParams.putAll(mPublicParams);
                }
            }

            if (isPublicHeaders) {
                if (mPublicHeaders != null && !mPublicHeaders.isEmpty()) {
                    mHeaders.putAll(mPublicHeaders);
                }
            }

            if (isGlobalFilter) {
                if (globalFilter != null) {
                    globalFilter.onPreRequest(mUrl, mHeaders, mParams);
                }
            }

            if (isSign) {
                if (signCallback != null) {
                    signCallback.sign(mUrl, mHeaders, mParams);
                } else if (globalSignCallback != null) {
                    globalSignCallback.sign(mUrl, mHeaders, mParams);
                }
            }

            OkHttpRequestBuilder builder = null;
            if (isPost) {
                switch (postType) {
                    case file:
                        break;
                    case form:
                        builder = OkHttpUtils
                                .post()
                                .params(mParams)
                                .headers(mHeaders);
                        break;
                    case json:
                        builder = OkHttpUtils
                                .postString()
                                .mediaType(mContentType)
                                .content(mParams.get("postString"))
                                .headers(mHeaders);
                        break;
                }


            } else {
                builder = OkHttpUtils.get().params(mParams).headers(mHeaders);
            }
            if (mTag != null) {
                builder.tag(mTag);
            }
            RequestCall build = builder.url(mUrl)
                    .build();
            build.execute(new StringCallback() {
                @Override
                public void onBefore(Request request, int id) {
                    if (callBack != null) {
                        callBack.onBefore();
                    }
                    if (mDialog != null) {
                        mDialog.show();
                    }
                    if (mLoadingLayout != null) {
                        mLoadingLayout.setStatus(BaseLoadingLayout.Loading);
                    }
                    if (isLog) {
                        logInfo.put("url", mUrl);
                        logInfo.put("params", mParams == null ? "none" : mParams);
                        logInfo.put("headers", mHeaders == null ? "none" : mHeaders);
                        if (request.body() != null) {
                            logInfo.put("Content-Type", request.body().contentType() == null ? "none" : request.body().contentType().toString());
                        }
                    }
                    super.onBefore(request, id);
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    error(e.getMessage());
                }


                private void error(String msg) {
                    //处理log
                    if (isLog) {
                        logInfo.put("response_error", msg);
                        outLog(logInfo.toJSONString());
                    }
                    //显示loadinglayout
                    if (mLoadingLayout != null) {
                        mLoadingLayout.setStatus(BaseLoadingLayout.Error);
                    }
                    //处理回掉
                    if (callBack != null) {
                        callBack.onError(msg);
                    }
                }

                @Override
                public void onResponse(String json, int id) {

                    boolean isSuccess = true;

                    try {
                        JSONObject o = JSON.parseObject(json);
                        logInfo.put("response_success", o);
                        if (isLog) {
                            outLog(logInfo.toJSONString());
                        }
                        if (isGlobalFilter) {
                            if (globalFilter != null) {
                                globalFilter.onPreResponse(o.getInteger("code"), json);
                            }
                        }
                    } catch (Exception e) {
//                                logInfo.put("response_error", json);
                        isSuccess = false;
                        error(json);
                    }

                    if (isSuccess) {
                        if (callBack != null) {
                            callBack.onResponse(json);
                        }
                    }


                }

                @Override
                public void onAfter(int id) {
                    if (callBack != null) {
                        callBack.onAfter();
                    }

                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    super.onAfter(id);
                }
            });

        }
    }

    private void outLog(String log) {
        if (isLogJson) {
            KLog.json(log);
        } else {
            KLog.d(log);
        }
    }

    /**
     * 取消网络任务
     *
     * @param tag
     */
    public static void cancel(Object tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
    }


    public enum PostType {
        form, json, file
    }


}

package com.robot.baseapi.net;

import java.util.Map;

/**
 * Created by lny on 2018/3/1.
 */


public interface SignCallback {
    /**
     * 进行签名
     * @param headers
     * @param params
     */
    void sign(String url,Map<String,String> headers,Map<String,String> params);
}

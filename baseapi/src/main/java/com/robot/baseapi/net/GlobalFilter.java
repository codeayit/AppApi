package com.robot.baseapi.net;

import java.util.HashMap;
import java.util.Map;

public interface GlobalFilter {
    void onPreResponse(int code,String json);
    void onPreRequest(String url, HashMap<String, String> headers, HashMap<String, String> params);
}

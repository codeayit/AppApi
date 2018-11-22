package com.robot.baseapi.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by lny on 2017/10/19.
 */

public class JsonParser {
    public JSONObject jo = null;

    public String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public JsonParser(String json) {
        jo = JSON.parseObject(json);
        this.json = json;
    }

    public JSONObject parseJSONObject(JSONObject jsonObject, String key) {
        if (jsonObject.containsKey(key)) {
            return jsonObject.getJSONObject(key);
        } else {
            throw new RuntimeException("network response not containt " + key + " filed");
        }
    }


    public JSONArray parseJSONArray(JSONObject jsonObject, String key) {
        if (jsonObject.containsKey(key)) {
            return jsonObject.getJSONArray(key);
        } else {
            throw new RuntimeException("network response not containt " + key + " filed");
        }
    }

    public <T> List<T> parseArray(JSONObject jsonObject, String key, Class<T> clazz) {
        if (jsonObject.containsKey(key)) {
            return JSON.parseArray(parseString(jsonObject, key), clazz);
        } else {
            throw new RuntimeException("network response not containt " + key + " filed");
        }
    }

    public <T> T parseObject(JSONObject jsonObject, String key, Class<? extends T> clazz) {
        if (jsonObject.containsKey(key)) {
            return (T) JSON.parseObject(parseString(jsonObject, key), clazz);
        } else {
            throw new RuntimeException("network response not containt " + key + " filed");
        }
    }

    public String parseString(JSONObject jsonObject, String key) {
        if (jsonObject.containsKey(key)) {
            return jsonObject.getString(key);
        } else {
            throw new RuntimeException("network response not containt " + key + " filed");
        }
    }


    public int parseInteger(JSONObject jsonObject, String key) {
        if (jsonObject.containsKey(key)) {
            return Integer.valueOf(jo.getString(key));
        } else {
            throw new RuntimeException("network response not containt " + key + " filed");
        }
    }

    public String toJsonString() {
        return json;
    }
    //-----------------------------------------------------------------------------------


}

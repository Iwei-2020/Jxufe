package com.smile.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 * @author smileplus
 */
public class JSONUtils {
    public static <T> T objToJavaBean(Object obj, Class<T> javaClass) {
        String jsonString = JSON.toJSONString(obj);
        return JSON.parseObject(jsonString, javaClass);
    }
    public static <T> List<T> objToList(Object obj, Class<T> javaClass) {
        String jsonString = JSON.toJSONString(obj);
        return JSONObject.parseArray(jsonString, javaClass);
    }
}

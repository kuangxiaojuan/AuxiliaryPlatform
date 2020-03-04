package com.terran.scheduled.api.utils;

import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonResult implements Serializable {
    private int code; //返回码 非1即失败
    private String msg;//消息提示
    private Map<String,Object> data;//返回的数据
    public JsonResult(){}
    public JsonResult(int code,String msg,Map<String,Object> data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public static String success() {
        return success(new HashMap(0));
    }
    public static String success(Map<String, Object> data) {
        return JSON.toJSONString(new JsonResult(1, "解析成功", data));
    }
    public static String failed() {
        return failed("解析失败");
    }
    public static String failed(String msg) {
        return failed(-1, msg);
    }

    public static String failed(int code, String msg) {
        return JSON.toJSONString(new JsonResult(code, msg, new HashMap(0)));
    }
}

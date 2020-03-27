package com.terran.scheduled.api.utils;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class JsonResult {
    private int code; // 1成功  0失败
    private String msg; // 消息
    private String data; //数据
    public JsonResult(){}
    public JsonResult(int code,String msg,String data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public static String  success(){
        return JSON.toJSONString(new JsonResult(1,"成功",""));
    }
    public static String  fail(){
        return JSON.toJSONString(new JsonResult(0,"失败",""));
    }
    public static String  fail(String data){
        return JSON.toJSONString(new JsonResult(0,"失败",data));
    }
}

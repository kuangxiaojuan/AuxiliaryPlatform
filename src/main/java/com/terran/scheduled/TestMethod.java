package com.terran.scheduled;

import com.terran.scheduled.api.annotation.Timer;
import com.terran.scheduled.api.annotation.TimerConfig;
import org.springframework.stereotype.Component;

@Component@TimerConfig
public class TestMethod {
    public void test1(){
        System.out.println("453454");
    }
    public void test1(String a,String b,String c,String f){
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(f);
        System.out.println("123654");
    }
    //仅第一次生效
    @Timer(name="测试自动定时任务",cronExpression = "10 * * * * ? ",remark = "XXX")
    public void test2(){
        System.out.println("--------------业务---------------");
    }
}

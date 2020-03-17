package com.terran.scheduled.api.annotation.handler;

import com.terran.scheduled.api.annotation.Timer;
import com.terran.scheduled.api.annotation.TimerConfig;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;
import java.util.Map;

public class TimerAutoConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Map<String,Object> beanMap = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(TimerConfig.class);
        System.out.println(beanMap);
        for (Object Obj : beanMap.values()) {
            Class objClass = Obj.getClass();
            for (Method method : objClass.getDeclaredMethods()) {
                if(method.isAnnotationPresent(Timer.class)){
                    dataBaseHandler(method,objClass);
                }
            }
        }
    }
    private void dataBaseHandler(Method method,Class objClass) {
        Timer timer = method.getAnnotation(Timer.class);
        String name = timer.name();//获取定时任务名称
        String cronExpression = timer.cronExpression();//获取cron表达式
        String remark = timer.remark();//备注信息
        System.out.println(name + "           " + cronExpression + "             "+remark);

    }
}

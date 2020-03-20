package com.terran.scheduled.api.annotation.handler;

import com.terran.scheduled.api.annotation.Timer;
import com.terran.scheduled.api.annotation.TimerConfig;
import com.terran.scheduled.api.model.SysJobConfig;
import com.terran.scheduled.api.service.ISysTaskService;
import com.terran.scheduled.api.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

public class TimerAutoConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ISysTaskService sysTaskService;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Map<String,Object> beanMap = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(TimerConfig.class);
        for (String beanName : beanMap.keySet()) {
            Class objClass = beanMap.get(beanName).getClass();
            for (Method method : objClass.getDeclaredMethods()) {
                if(method.isAnnotationPresent(Timer.class)){
                    try{
                        dataBaseHandler(method,objClass,beanName);
                    }catch (Exception e){e.printStackTrace();}
                }
            }
        }
    }
    private void dataBaseHandler(Method method,Class objClass,String beanName) throws Exception{
        Timer timer = method.getAnnotation(Timer.class);
        String name = timer.name();//获取定时任务名称
        String cronExpression = timer.cronExpression();//获取cron表达式
        String remark = timer.remark();//备注信息
        String cronMethodName = method.getName();//方法名称
        String cronBeanName = beanName;//类名称
        String methodParams = "";
        for (Field field : objClass.getFields()) {
            String paramName = field.getName();
            methodParams += paramName;
        }
        SysJobConfig sysJobConfig = sysTaskService.findByBeanNameAndMethodNameAndMethodParams(cronBeanName,cronMethodName,methodParams);
        if(sysJobConfig==null||StringUtils.isEmpty(sysJobConfig.getJobId())) {
            dataBaseHandlerSaveOrUpdate(new SysJobConfig(),name,cronBeanName,
                    cronMethodName,methodParams,cronExpression,remark,new Date(),new Date());
        }
    }
    private void dataBaseHandlerSaveOrUpdate(
            SysJobConfig sysJobConfig,String name,
            String cronBeanName,String cronMethodName,
            String methodParams,String cronExpression,String remark,Date createTime,Date updateTime) throws Exception{
        sysJobConfig.setJobId(sysJobConfig.getJobId());
        sysJobConfig.setName(name);
        sysJobConfig.setBeanName(cronBeanName);
        sysJobConfig.setMethodName(cronMethodName);
        sysJobConfig.setJobStatus(1);
        sysJobConfig.setMethodParams(methodParams);
        sysJobConfig.setCronExpression(cronExpression);
        sysJobConfig.setRemark(remark);
        sysJobConfig.setCreateTime(createTime);
        sysJobConfig.setUpdateTime(updateTime);
        sysTaskService.save(sysJobConfig);
    }
}

package com.terran.scheduled.api.config;



import com.terran.scheduled.api.model.SysJobConfig;
import com.terran.scheduled.api.model.SysJobLog;
import com.terran.scheduled.api.service.ISysJobLogService;
import com.terran.scheduled.api.service.ISysTaskService;
import com.terran.scheduled.api.utils.SpringContextUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
public class SchedulingRunnable implements Runnable{
    /**
     * Bean 名称
     */
    @Getter
    private String beanName;

    /**
     * 方法 名称
     */
    @Getter
    private String methodName;
    /**
     * 参数
     */
    @Getter
    private Object[] params;

    public SchedulingRunnable(String beanName, String methodName) {
        this.beanName = beanName;
        this.methodName = methodName;
    }

    public SchedulingRunnable(String beanName, String methodName, Object... params) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
    }
    //校验是否存在定时任务方法
    public void schedulingValidate() throws Exception{
        Object obj = SpringContextUtil.getBean(beanName);
        Method method = null;
        if (!StringUtils.isEmpty(params)&&params.length>0) {
            Class<?>[] paramCls = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                paramCls[i] = params[i].getClass();
            }
            method = obj.getClass().getDeclaredMethod(methodName, paramCls);
        } else {
            method = obj.getClass().getDeclaredMethod(methodName);
        }
    }
    //日志记录
    private void setSysJobConfig(Date startTime,Date endTime,long time,String className,String methodName,boolean resultFlag,String throwsException) {
        ISysTaskService sysTaskService = (ISysTaskService)SpringContextUtil.getBean("sysTaskServiceImp");
        ISysJobLogService sysJobLogService = (ISysJobLogService) SpringContextUtil.getBean("sysJobLogServiceImp");
        //目前只支持string类型的参数
        String paramTemps = "";
        if(params!=null){
            for (Object param : params) {
                paramTemps += param + ";";
            }
            paramTemps = paramTemps.substring(0,paramTemps.length() - 1);
        }
        try {
            SysJobConfig sysJobConfig = sysTaskService.findByBeanNameAndMethodNameAndMethodParams(beanName, methodName, paramTemps);
            if(sysJobConfig.getJobId()!= -1){
                SysJobLog sysJobLog = new SysJobLog();
                sysJobLog.setForeignId(sysJobConfig.getJobId());
                sysJobLog.setStartTime(startTime);
                sysJobLog.setEndTime(endTime);
                sysJobLog.setClassName(className);
                sysJobLog.setMethodName(methodName);
                sysJobLog.setTime(time);
                sysJobLog.setTitleName(sysJobConfig.getName());
                sysJobLog.setResultFlag(resultFlag);
                if(resultFlag == Boolean.FALSE)sysJobLog.setThrowsException(throwsException);
                sysJobLogService.save(sysJobLog);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void run() {
        log.info("定时任务开始执行：beanName:{},methodName:{},params:{}", beanName, methodName, params);

        // 开始执行时间
        long startTime = System.currentTimeMillis();

        try {
            // 获取到bean
            Object target = SpringContextUtil.getBean(beanName);

            // 获取到bean里面的方法
            Method method = null;
            if (!StringUtils.isEmpty(params)&&params.length>0) {
                Class<?>[] paramCls = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    paramCls[i] = params[i].getClass();
                }
                method = target.getClass().getDeclaredMethod(methodName, paramCls);
            } else {
                method = target.getClass().getDeclaredMethod(methodName);
            }
            ReflectionUtils.makeAccessible(method);//将一个方法设置为可调用，主要针对private方法;
            if (null != params && params.length > 0) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }

            //日志记录
            long times = System.currentTimeMillis() - startTime;
            log.info("定时任务执行结束 - bean：{}，方法：{}，参数：{}，耗时：{} 毫秒", beanName, methodName, params, times);

            this.setSysJobConfig(new Date(startTime),new Date(),times,beanName,methodName,true,"");
        } catch (Exception e) {
            long times = System.currentTimeMillis() - startTime;
            log.error(String.format("定时任务执行异常 - bean：%s，方法：%s，参数：%s ", beanName, methodName, params), e);
            this.setSysJobConfig(new Date(startTime),new Date(),times,beanName,methodName,false,e.getMessage());
        }

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingRunnable that = (SchedulingRunnable) o;
        if (params == null) {
            return beanName.equals(that.beanName) &&
                    methodName.equals(that.methodName) &&
                    that.params == null;
        }
        //同理没注意params是数组对象，地址不一致，用Arrays.equals轮询相等
        return beanName.equals(that.beanName) &&
                methodName.equals(that.methodName) &&
                Arrays.equals(params,that.params);
    }
    @Override
    public int hashCode() {
        if (params == null) {
            return Objects.hash(beanName, methodName);
        }
        //坑爹啊，数组是没有hashCode()方法，数组用Arrays.hashCode()包起来
        return Objects.hash(beanName, methodName, Arrays.hashCode(params));
    }
}

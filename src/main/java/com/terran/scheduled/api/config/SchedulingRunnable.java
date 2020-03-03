package com.terran.scheduled.api.config;


import com.terran.scheduled.api.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
public class SchedulingRunnable implements Runnable{
    /**
     * Bean 名称
     */
    private String beanName;

    /**
     * 方法 名称
     */
    private String methodName;
    /**
     * 参数
     */
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
        if (null != params && params.length > 0) {
            Class<?>[] paramCls = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                paramCls[i] = params[i].getClass();
            }
            method = obj.getClass().getDeclaredMethod(methodName, paramCls);
        } else {
            method = obj.getClass().getDeclaredMethod(methodName);
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
            if (null != params && params.length > 0) {
                Class<?>[] paramCls = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    paramCls[i] = params[i].getClass();
                }
                method = target.getClass().getDeclaredMethod(methodName, paramCls);
            } else {
                method = target.getClass().getDeclaredMethod(methodName);
            }
            ReflectionUtils.makeAccessible(method);
            if (null != params && params.length > 0) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
        } catch (Exception e) {
            log.error(String.format("定时任务执行异常 - bean：%s，方法：%s，参数：%s ", beanName, methodName, params), e);
        }
        long times = System.currentTimeMillis() - startTime;
        log.info("定时任务执行结束 - bean：{}，方法：{}，参数：{}，耗时：{} 毫秒", beanName, methodName, params, times);
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

        return beanName.equals(that.beanName) &&
                methodName.equals(that.methodName) &&
                params.equals(that.params);
    }
    @Override
    public int hashCode() {
        if (params == null) {
            return Objects.hash(beanName, methodName);
        }

        return Objects.hash(beanName, methodName, params);
    }
}

package com.terran.scheduled.api.config;

import java.util.concurrent.ScheduledFuture;
/**
 * 配置ScheduledFuture的包装类。ScheduledFuture是ScheduledExecutorService定时任务线程池的执行结果
 */
public class ScheduledTask {
    volatile ScheduledFuture<?> future;

    /**
     * 取消 定时任务
     */
    public void cancel(){
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}

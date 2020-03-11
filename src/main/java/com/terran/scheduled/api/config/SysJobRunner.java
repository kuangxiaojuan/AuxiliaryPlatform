package com.terran.scheduled.api.config;

import com.alibaba.fastjson.JSON;
import com.terran.scheduled.api.model.SysJobConfig;
import com.terran.scheduled.api.service.ISysTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysJobRunner implements CommandLineRunner {
    private final static Logger log = LoggerFactory.getLogger(SysJobRunner.class);
    @Autowired
    private ISysTaskService sysTaskService;
    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;
    @Override
    public void run(String... args) throws Exception {
        List<SysJobConfig> list = sysTaskService.selectTasks();
        log.info(">>>>初始化定时任务 list={}", JSON.toJSON(list).toString());
        for (SysJobConfig jobVo : list) {
            SchedulingRunnable task = null;
            if(StringUtils.isEmpty(jobVo.getMethodParams()))
                task = new SchedulingRunnable(jobVo.getBeanName(), jobVo.getMethodName(), null);
            else
                task = new SchedulingRunnable(jobVo.getBeanName(), jobVo.getMethodName(), jobVo.getMethodParams().split(";"));
            cronTaskRegistrar.addCronTask(task, jobVo.getCronExpression());
        }
        log.info(">>>>>定时任务初始化完毕<<<<<");
    }
}

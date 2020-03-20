package com.terran.scheduled.api.service;

import com.terran.scheduled.api.config.ScheduledTask;
import com.terran.scheduled.api.model.SysJobConfig;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ISysTaskService {
    List<SysJobConfig> selectTasks() throws Exception;

    Page<SysJobConfig> selectTaskByPage(int status, int pageNum, int pageSize) throws Exception;

    void addTask(SysJobConfig jobVo) throws Exception;

    void updateTask(SysJobConfig jobVo) throws Exception;

    void deleteTask(Integer jobId) throws Exception;

    SysJobConfig selectTask(int id) throws Exception;

    SysJobConfig findByBeanNameAndMethodNameAndMethodParams(String beanName,String method,String params) throws Exception;

    SysJobConfig save(SysJobConfig sysJobConfig) throws Exception;

    public SysJobConfig saveAndFlush(SysJobConfig sysJobConfig) throws Exception;

    public Map<Runnable, ScheduledTask> selectRunningJob() throws Exception;
}

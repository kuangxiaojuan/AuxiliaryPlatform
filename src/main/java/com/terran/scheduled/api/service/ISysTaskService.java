package com.terran.scheduled.api.service;

import com.terran.scheduled.api.model.SysJobConfig;

import java.util.List;

public interface ISysTaskService {
    public List<SysJobConfig> selectTask(int status);

    public void addTask(SysJobConfig jobVo);

    public void updateTask(SysJobConfig jobVo);

    public void deleteTask(Integer jobId);

}

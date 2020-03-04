package com.terran.scheduled.api.service;

import com.terran.scheduled.api.model.SysJobConfig;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISysTaskService {
    public List<SysJobConfig> selectTask(int status) throws Exception;

    public Page<SysJobConfig> selectTaskByPage(int status, int pageNum, int pageSize) throws Exception;

    public void addTask(SysJobConfig jobVo) throws Exception;

    public void updateTask(SysJobConfig jobVo) throws Exception;

    public void deleteTask(Integer jobId) throws Exception;

}

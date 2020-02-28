package com.terran.scheduled.api.service;

import com.terran.scheduled.api.config.CronTaskRegistrar;
import com.terran.scheduled.api.config.SchedulingRunnable;
import com.terran.scheduled.api.dao.SysAppConfigDao;
import com.terran.scheduled.api.dao.SysJobConfigDao;
import com.terran.scheduled.api.model.SysAppConfig;
import com.terran.scheduled.api.model.SysJobConfig;
import com.terran.scheduled.api.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Service
public class SysTaskServiceImpl  implements ISysTaskService{
    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;
    @Autowired
    private SysJobConfigDao sysJobConfigDao;
    /**
     * 查询进行中的定时任务类
     * 状态 0=正常；1=暂停
     * @param status
     */
    @Override
    public List<SysJobConfig> selectTask(int status) throws Exception{
        List<SysJobConfig> jobList = sysJobConfigDao.findAll();
        List<SysJobConfig> list = jobList.stream()
                .filter(s -> Objects.equals(s.getJobStatus(), status))
                .collect(Collectors.toList());
        return list;
    }
    /**
     * 添加定时任务
     *
     * @param jobVo
     */
    @Override
    public void addTask(SysJobConfig jobVo) throws Exception{
        // 处理数据 插入数据库
        jobVo = sysJobConfigDao.save(jobVo);
        // 判断定时任务是否开启
        Integer jobStatus = jobVo.getJobStatus();
        if (Objects.equals(jobStatus, 0)) {
            this.changeTaskStatus(Boolean.TRUE, jobVo);
        }

    }
    /**
     * 修改定时任务
     *
     * @param jobVo
     */
    @Override
    public void updateTask(SysJobConfig jobVo) throws Exception{
        // 获取数据库中已存在的数据
        SysJobConfig existJob = sysJobConfigDao.getOne(jobVo.getJobId());
        // 判断 原来的定时任务是否开启，如果开启，则先停止
        if (Objects.equals(existJob.getJobStatus(), 0)) {
            this.changeTaskStatus(Boolean.FALSE, existJob);
        }
        // 处理数据 插入数据库
        sysJobConfigDao.save(jobVo);
        // 判断定时任务是否开启
        if (Objects.equals(jobVo.getJobStatus(), 0)) {
            this.changeTaskStatus(Boolean.TRUE, jobVo);
        }
    }
    /**
     * 删除定时任务
     *
     * @param jobId
     */
    @Override
    public void deleteTask(Integer jobId) throws Exception{
        // 获取数据库中已存在的数据
        SysJobConfig existJob = sysJobConfigDao.getOne(jobId);
        // 判断定时任务是否开启
        if (Objects.equals(existJob.getJobStatus(), 0)) {
            this.changeTaskStatus(Boolean.FALSE, existJob);
        }
        // 处理数据 删除数据
        sysJobConfigDao.deleteById(jobId);
    }
    /**
     * 修改定时任务类状态
     *
     * @param add
     * @param jobVo
     */
    private void changeTaskStatus(boolean add, SysJobConfig jobVo) throws Exception{
        if (add) {
            SchedulingRunnable task = new SchedulingRunnable(jobVo.getBeanName(), jobVo.getMethodName(), jobVo.getMethodParams());
            cronTaskRegistrar.addCronTask(task, jobVo.getCronExpression());
        } else {
            SchedulingRunnable task = new SchedulingRunnable(jobVo.getBeanName(), jobVo.getMethodName(), jobVo.getMethodParams());
            cronTaskRegistrar.removeCronTask(task);
        }
    }
}

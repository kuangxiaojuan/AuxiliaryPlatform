package com.terran.scheduled.api.service;

import com.alibaba.fastjson.JSON;
import com.terran.scheduled.api.config.CronTaskRegistrar;
import com.terran.scheduled.api.config.SchedulingRunnable;
import com.terran.scheduled.api.dao.SysJobConfigDao;
import com.terran.scheduled.api.model.SysJobConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class SysTaskServiceImpl  implements ISysTaskService{
    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;
    @Autowired
    private SysJobConfigDao sysJobConfigDao;
    /**
     * 查询进行中的定时任务类,不分页
     * 状态
     * @param status
     * @return
     */
    @Override
    public List<SysJobConfig> selectTasks() throws Exception{
        List<SysJobConfig> jobList = sysJobConfigDao.findAll();
        return jobList;
    }
    public SysJobConfig selectTask(int id) throws Exception{
        return sysJobConfigDao.getOne(id);
    }
    /**
     * 查询进行中的定时任务类,分页
     * 状态 0=正常；1=暂停
     * @param status
     */
    @Override
    public Page<SysJobConfig> selectTaskByPage(int status, int pageNum, int pageSize) throws Exception{
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        Specification<SysJobConfig> sysJobConfigSpecification = new Specification<SysJobConfig>() {
            @Override
            public Predicate toPredicate(Root<SysJobConfig> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<String> jobStatus = root.get("jobStatus");
                return criteriaBuilder.equal(jobStatus,status);
            }
        };
        return sysJobConfigDao.findAll(sysJobConfigSpecification,pageable);
    }
    /**
     * 添加定时任务
     *
     * @param jobVo
     */
    @Override
    public void addTask(SysJobConfig jobVo) throws Exception{
        // 开启定时任务
        Integer jobStatus = jobVo.getJobStatus();
        if (Objects.equals(jobStatus, 1)) {
            this.changeTaskStatus(Boolean.TRUE, jobVo);
        }
        // 处理数据 插入数据库
        if(StringUtils.isEmpty(jobVo.getJobId())) {
            jobVo.setCreateTime(new Date());
            jobVo.setUpdateTime(new Date());
        }else jobVo.setUpdateTime(new Date());

        jobVo = sysJobConfigDao.save(jobVo);
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
        jobVo = sysJobConfigDao.save(jobVo);
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
        SchedulingRunnable task = null;
        if(StringUtils.isEmpty(jobVo.getMethodParams()))
            task = new SchedulingRunnable(jobVo.getBeanName(), jobVo.getMethodName(), null);
        else
            task = new SchedulingRunnable(jobVo.getBeanName(), jobVo.getMethodName(), jobVo.getMethodParams().split(";"));
        if (add) {
            task.schedulingValidate();//判断是否有该方法
            cronTaskRegistrar.addCronTask(task, jobVo.getCronExpression());
        } else {
            cronTaskRegistrar.removeCronTask(task);
        }
    }
}

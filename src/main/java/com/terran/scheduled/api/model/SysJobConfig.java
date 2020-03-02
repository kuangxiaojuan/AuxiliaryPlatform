package com.terran.scheduled.api.model;

import com.terran.scheduled.api.annotation.IsCron;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 定时任务列表
 */
@Entity
@Table(name = "sys_job_config")
@Data
public class SysJobConfig {
    /**
     * 定时任务ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;
    /**
     * 定时任务名称
     */
    @NotEmpty(message = "定时任务名称不得为空")
    @Column(name = "name",length = 100)
    private String name;
    /**
     * bean 名称
     */
    @NotEmpty(message = "类名称不得为空")
    @Column(name = "bean_name",length = 100)
    private String beanName;

    /**
     * 方法名称
     */
    @NotEmpty(message = "方法名称不得为空")
    @Column(name = "method_name",length = 100)
    private String methodName;

    /**
     * 方法参数
     */
    @Column(name = "method_params",length = 100)
    private String methodParams;

    /**
     * cron表达式
     */
    @IsCron@NotEmpty(message = "cron表达式不得为空")
    @Column(name = "cron_expression",length = 100)
    private String cronExpression;

    /**
     * 状态 0=正常；1=暂停
     */
    @Column(name = "job_status")
    private Integer jobStatus;

    /**
     * 备注
     */
    @Column(name = "remark",length = 500)
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}

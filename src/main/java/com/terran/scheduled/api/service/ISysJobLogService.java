package com.terran.scheduled.api.service;


import com.terran.scheduled.api.model.SysJobLog;

import java.util.List;

public interface ISysJobLogService {
    public List<SysJobLog> getScheduledList(int jobId) ;

    public SysJobLog getSysJobLogById(int id) ;

    public void save(SysJobLog sysJobLog);
}

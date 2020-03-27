package com.terran.log.service;

import com.terran.log.model.SysJobLog;

import java.util.List;
import java.util.Optional;

public interface ISysJobLogService {
    public List<SysJobLog> getScheduledList(int id) throws Exception;

    public Optional<SysJobLog> getSysJobLogById(int id) ;
}

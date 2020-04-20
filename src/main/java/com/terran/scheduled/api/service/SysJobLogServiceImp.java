package com.terran.scheduled.api.service;


import com.terran.scheduled.api.dao.SysJobLogDao;
import com.terran.scheduled.api.model.SysJobLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SysJobLogServiceImp implements ISysJobLogService {
    @Autowired
    private SysJobLogDao sysJobLogDao;
    @Override
    public List<SysJobLog> getScheduledList(int jobId)  {
        try{
            return  sysJobLogDao.findByForeignId(jobId);
        }catch(Exception e){}
        return null;
    }
    public SysJobLog getSysJobLogById(int id)  {
        Optional<SysJobLog> sysJobLogOpt =  sysJobLogDao.findById(id);
        if(sysJobLogOpt.isPresent()) return sysJobLogOpt.get();
        return null;
    }

    @Override
    public void save(SysJobLog sysJobLog) {
        sysJobLogDao.save(sysJobLog);
    }

}

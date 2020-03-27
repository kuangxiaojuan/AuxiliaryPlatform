package com.terran.log.service;

import com.terran.log.dao.SysJobLogDao;
import com.terran.log.model.SysJobLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SysJobLogServiceImp implements ISysJobLogService {
    @Autowired
    private SysJobLogDao sysJobLogDao;
    @Override
    public List<SysJobLog> getScheduledList(int id) throws Exception {
        return sysJobLogDao.findByForeignId(id);
    }
    public Optional<SysJobLog> getSysJobLogById(int id)  {
        return sysJobLogDao.findById(id);
    }
}

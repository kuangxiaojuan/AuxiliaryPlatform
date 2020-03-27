package com.terran.log.dao;

import com.terran.log.model.SysJobLog;
import com.terran.scheduled.api.model.SysJobConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysJobLogDao extends JpaRepository<SysJobLog,Integer> {
    List<SysJobLog> findByForeignId(int id) throws Exception;
}

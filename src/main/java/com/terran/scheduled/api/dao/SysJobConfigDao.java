package com.terran.scheduled.api.dao;

import com.terran.scheduled.api.model.SysJobConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysJobConfigDao  extends JpaRepository<SysJobConfig,Integer>, JpaSpecificationExecutor<SysJobConfig> {
    List<SysJobConfig> findByJobStatus(int jobStatus);
}

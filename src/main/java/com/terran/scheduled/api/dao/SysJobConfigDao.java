package com.terran.scheduled.api.dao;

import com.terran.scheduled.api.model.SysAppConfig;
import com.terran.scheduled.api.model.SysJobConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysJobConfigDao  extends JpaRepository<SysJobConfig,Integer> {
}

package com.terran.scheduled.api.dao;

import com.terran.scheduled.api.model.SysAppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysAppConfigDao extends JpaRepository<SysAppConfig,Integer> {
    SysAppConfig findByFdKey(String key);
}

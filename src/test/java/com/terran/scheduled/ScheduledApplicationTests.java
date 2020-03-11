package com.terran.scheduled;

import com.terran.scheduled.api.config.CronTaskRegistrar;
import com.terran.scheduled.api.config.SchedulingRunnable;
import com.terran.scheduled.api.service.ISysTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ScheduledApplicationTests {
    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;
    @Autowired
    private ISysTaskService sysTaskService;
    @Test
    void contextLoads() {
        SchedulingRunnable task1 = new SchedulingRunnable("testMethod","test1" );
        task1.run();
        SchedulingRunnable task2 = new SchedulingRunnable("testMethod","test1","1","2","3");
        task2.run();
    }
    @Test
    void testMap(){
        String[] txt = new String[]{"1","2","3","4"};
        SchedulingRunnable task1 = new SchedulingRunnable("testMethod","test1",txt);
        SchedulingRunnable task2 = new SchedulingRunnable("testMethod","test1",txt);
    }
}

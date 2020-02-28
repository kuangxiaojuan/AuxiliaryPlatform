package com.terran.scheduled;

import com.terran.scheduled.api.config.SchedulingRunnable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScheduledApplicationTests {

    @Test
    void contextLoads() {
        SchedulingRunnable task1 = new SchedulingRunnable("testMethod","test1" );
        task1.run();
        SchedulingRunnable task2 = new SchedulingRunnable("testMethod","test1","1","2","3");
        task2.run();
    }

}

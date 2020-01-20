package com.terran.scheduled;

import com.terran.scheduled.api.config.SchedulingRunnable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScheduledApplicationTests {

    @Test
    void contextLoads() {
        SchedulingRunnable task = new SchedulingRunnable("testMethod","test1" );
        task.run();
    }

}

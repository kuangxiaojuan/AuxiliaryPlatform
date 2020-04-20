package com.terran.scheduled.api.action;

import com.terran.scheduled.api.model.SysJobLog;
import com.terran.scheduled.api.service.ISysJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SysLogAction {
    @Autowired
    private ISysJobLogService sysJobLogService;

    @RequestMapping(value = "/logs/{jobId}",method = RequestMethod.GET)
    public List<SysJobLog> getSysJobLog(@PathVariable Integer jobId){
        return sysJobLogService.getScheduledList(jobId);
    }
}

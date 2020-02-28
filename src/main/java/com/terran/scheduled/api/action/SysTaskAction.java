package com.terran.scheduled.api.action;

import com.alibaba.fastjson.JSON;
import com.terran.scheduled.api.model.SysJobConfig;
import com.terran.scheduled.api.service.ISysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Controller@ResponseBody
@Transactional(rollbackOn = Exception.class)
public class SysTaskAction {
    @Autowired
    private ISysTaskService sysTaskService;

    @RequestMapping(value = "/tasks/{pageNum}/{pageSize}",method = RequestMethod.GET)
    public List<SysJobConfig> getTaskPage(@PathVariable("pageNum")int pageNum,
                                          @PathVariable(value = "pageSize",required = false) int pageSize,
                                          @RequestParam(value = "status",required = false) int status) throws Exception{
        Page<SysJobConfig> sysJobConfigPages= null;
        if(StringUtils.isEmpty(pageSize))
            sysJobConfigPages= sysTaskService.selectTaskByPage(status,pageNum,15);
        else if(StringUtils.isEmpty(status))
            sysJobConfigPages= sysTaskService.selectTaskByPage(3,pageNum,pageSize);
        else if(StringUtils.isEmpty(status)&&StringUtils.isEmpty(pageSize))
            sysJobConfigPages= sysTaskService.selectTaskByPage(3,pageNum,pageSize);
        else
            sysJobConfigPages= sysTaskService.selectTaskByPage(status,pageNum,pageSize);

        System.out.println(sysJobConfigPages.getContent());
        System.out.println(sysJobConfigPages.getTotalElements());
        System.out.println(sysJobConfigPages.getTotalPages());
        return sysJobConfigPages.get().collect(Collectors.toList());
    }

    /**
     * 定时任务列表,0=正常；1=暂停
     * @param status
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/tasks",method = RequestMethod.GET)
    public List<SysJobConfig> getTaskList(int status) throws Exception{
        return sysTaskService.selectTask(status);
    }
}

package com.terran.scheduled.api.action;

import com.alibaba.fastjson.JSON;
import com.terran.scheduled.api.constant.ScheduledConstant;
import com.terran.scheduled.api.model.SysJobConfig;
import com.terran.scheduled.api.service.ISysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller@ResponseBody
@Transactional(rollbackOn = Exception.class)
public class SysTaskAction {
    @Autowired
    private ISysTaskService sysTaskService;

    /**
     * 定时任务列表,0=正常；1=暂停
     * @param status
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/tasks",method = RequestMethod.GET)
    public String  getTaskList(int status) throws Exception{
        return JSON.toJSON(sysTaskService.selectTask(status)).toString();

    }
    @RequestMapping(value = "/task/add",method = RequestMethod.GET)
    public String  addTaskList() throws Exception{
        return "/scheduled/add";
    }
    @RequestMapping(value = "/task/save",method = RequestMethod.POST)
    public String  saveTaskList(SysJobConfig sysJobConfig) throws Exception{
        sysTaskService.addTask(sysJobConfig);
        return "success";
    }
}

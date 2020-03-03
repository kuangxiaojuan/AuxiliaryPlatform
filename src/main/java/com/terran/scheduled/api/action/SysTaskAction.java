package com.terran.scheduled.api.action;

import com.alibaba.fastjson.JSON;
import com.terran.scheduled.api.constant.ScheduledConstant;
import com.terran.scheduled.api.model.SysJobConfig;
import com.terran.scheduled.api.service.ISysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller@ResponseBody
@Transactional(rollbackOn = Exception.class)
public class SysTaskAction {
    @Autowired
    private ISysTaskService sysTaskService;
    private Object[] arguments;

    /**
     * 定时任务列表
     * @param status
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/tasks",method = RequestMethod.GET)
    public List<SysJobConfig>  getTaskList(int status) throws Exception{
        return sysTaskService.selectTask(status);

    }
    @RequestMapping(value = "/task/save",method = RequestMethod.POST)
    public Map<String, Object>  saveTaskList(@Valid SysJobConfig sysJobConfig, BindingResult bindingResult) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        //JSR303校验
        if(bindingResult.hasErrors()){
            List<ObjectError> errorList = bindingResult.getAllErrors();
            List<String> mesList = new ArrayList<String>();
            for (ObjectError objectError : errorList) mesList.add(objectError.getDefaultMessage());
            map.put("status", false);
            map.put("error", mesList);
        } else {
            sysTaskService.addTask(sysJobConfig);
            map.put("status", true);
            map.put("msg", "添加成功");
        }
        return map;
    }

    /**
     * cron表达式校验
     * @param cronValue
     * @return
     */
    @RequestMapping(value = "/validate/cron",method = RequestMethod.GET)
    public boolean cronValidate(String cronValue){
        return CronSequenceGenerator.isValidExpression(cronValue);
    }
}

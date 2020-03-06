package com.terran.scheduled.api.action;

import com.alibaba.fastjson.JSON;
import com.terran.scheduled.api.model.SysJobConfig;
import com.terran.scheduled.api.service.ISysTaskService;
import com.terran.scheduled.api.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller@ResponseBody
@Transactional(rollbackOn = Exception.class)
public class SysTaskAction {
    @Autowired
    private ISysTaskService sysTaskService;

    @RequestMapping(value = "/tasks",method = RequestMethod.GET)
    public List<SysJobConfig>  getTaskList() throws Exception{
        return sysTaskService.selectTasks();
    }
    @RequestMapping(value = "/task/{id}",method = RequestMethod.GET)
    public SysJobConfig getTask(@PathVariable int id) throws Exception{
        return sysTaskService.selectTask(id);
    }
    @RequestMapping(value = "/task/{id}",method = RequestMethod.DELETE)
    public String delTask(@PathVariable int id) throws Exception{
        sysTaskService.deleteTask(id);
        return JsonResult.success();
    }
    @RequestMapping(value = "/task/save",method = RequestMethod.POST)
    public String  saveTaskList(@Valid SysJobConfig sysJobConfig, BindingResult bindingResult) throws Exception{
        //JSR303校验
        if(bindingResult.hasErrors()){
            List<ObjectError> errorList = bindingResult.getAllErrors();
            List<String> mesList = new ArrayList<String>();
            for (ObjectError objectError : errorList) mesList.add(objectError.getDefaultMessage());
            return JsonResult.fail(JSON.toJSONString(mesList));
        } else {
            sysTaskService.addTask(sysJobConfig);
            return JsonResult.success();
        }
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

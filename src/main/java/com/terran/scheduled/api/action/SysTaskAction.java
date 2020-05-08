package com.terran.scheduled.api.action;

import com.alibaba.fastjson.JSON;
import com.terran.scheduled.api.config.ScheduledTask;
import com.terran.scheduled.api.config.SchedulingRunnable;
import com.terran.scheduled.api.model.SysJobConfig;
import com.terran.scheduled.api.service.ISysTaskService;
import com.terran.scheduled.api.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Transactional(rollbackOn = Exception.class)
public class SysTaskAction {
    @Autowired
    private ISysTaskService sysTaskService;

    /**
     * 获取所有定时任务
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/tasks",method = RequestMethod.GET)
    public List<SysJobConfig>  getTaskList() throws Exception{
        return sysTaskService.selectTasks();
    }

    /**
     * 获取当前执行定时任务列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/runningTasks",method = RequestMethod.GET)
    public List<String> getRunningJob() throws Exception{
        List<String> list = new ArrayList<>();
        sysTaskService.selectRunningJob().forEach((running,scheduledTask)->{
            SchedulingRunnable schedulingRunnable = (SchedulingRunnable)running;
            String values = schedulingRunnable.getBeanName() + "==" + schedulingRunnable.getMethodName();
            if(schedulingRunnable.getParams()!=null){
                String param = "";
                for (String s : (String[]) schedulingRunnable.getParams()) {
                    param +=  s +";";
                }
                values += "=="+ param;
            }
            list.add(values);
        });
        return list;
    }

    /**
     * 获取指定定时任务
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/task/{id}",method = RequestMethod.GET)
    public SysJobConfig getTask(@PathVariable int id) throws Exception{
        return sysTaskService.selectTask(id);
    }

    /**
     * 删除指定定时任务
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/task/{id}",method = RequestMethod.DELETE)
    public String delTask(@PathVariable int id) throws Exception{
        sysTaskService.deleteTask(id);
        return JsonResult.success();
    }

    /**
     * 更新添加定时任务
     * @param sysJobConfig
     * @param bindingResult
     * @return
     * @throws Exception
     */
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
    public boolean cronValidate(String cronValue) throws Exception{
        return CronSequenceGenerator.isValidExpression(cronValue);
    }
    @RequestMapping(value = "/task/next/{id}",method = RequestMethod.GET)
    public List<String> cronNextTask(@PathVariable Integer id) throws Exception {
       SysJobConfig sysJobConfig = sysTaskService.selectTask(id);
        List<String> retlist = new ArrayList<>();

       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       if(CronSequenceGenerator.isValidExpression(sysJobConfig.getCronExpression())){
           CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(sysJobConfig.getCronExpression());
           Date currentTime = new Date();

           Date time1 = cronSequenceGenerator.next(currentTime);
           Date time2 = cronSequenceGenerator.next(time1);
           Date time3 = cronSequenceGenerator.next(time2);
           Date time4 = cronSequenceGenerator.next(time3);
           Date time5 = cronSequenceGenerator.next(time4);
           Date time6 = cronSequenceGenerator.next(time5);

           retlist.add(sdf.format(time2));//下一次运行时间
           retlist.add(sdf.format(time3));//下一次运行时间
           retlist.add(sdf.format(time4));//下一次运行时间
           retlist.add(sdf.format(time5));//下一次运行时间
           retlist.add(sdf.format(time6));//下一次运行时间
       }
        return retlist;
    }
}

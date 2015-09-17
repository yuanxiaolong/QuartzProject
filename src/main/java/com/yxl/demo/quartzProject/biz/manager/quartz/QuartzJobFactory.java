package com.yxl.demo.quartzProject.biz.manager.quartz;



import com.yxl.demo.quartzProject.common.MonitorItemTypeEnum;
import com.yxl.demo.quartzProject.dataobject.ScheduleJob;
import com.yxl.demo.quartzProject.util.SpringBeanHelper;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * 动态Job工厂
 */
@DisallowConcurrentExecution
public class QuartzJobFactory implements Job , Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(QuartzJobFactory.class);

    private static final long serialVersionUID = -3L; //固定编号,以免自动生成quartz跟数据库对比产生不一致

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //1.获取封装体
        ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");
        LOG.info("start Job [ " + scheduleJob.getJobName() + " ]");

        //2.获取类型
        String monitorType = scheduleJob.getMonitorType();
        String bussNo = scheduleJob.getBussNo();
        String monitorName = scheduleJob.getJobName();
        String monitorDesc = scheduleJob.getDesc();
        String execContent = scheduleJob.getExecContent();
        String orgs = scheduleJob.getOrgs();
        String templateId = scheduleJob.getTemplateId();
        String maxRetryTimes = scheduleJob.getMaxRetryTimes();
        String retryPeriod = scheduleJob.getRetryPeriod();

        String monitorResult = "";

        // FIXME TODO 这里可以以后抽象成 监控工厂,把判断逻辑下移
        if (StringUtils.equalsIgnoreCase(MonitorItemTypeEnum.SHELL.getCode(),monitorType)){
//   FIXME  TODO       MonitorShellManager monitorShellManager = (MonitorShellManager) SpringBeanHelper.getBean("monitorShellManager");
//            monitorResult = monitorShellManager.monitor(monitorName,monitorDesc,execContent);
        }else if (StringUtils.equalsIgnoreCase(MonitorItemTypeEnum.SYS.getCode(),monitorType)){
            LOG.info("to be continue :) ");
        }else{
            LOG.warn("unknow monitor type : " + monitorType);
        }
        //3.如果监控结果不为空,则报警
        if (StringUtils.isNotBlank(monitorResult)){
//    FIXME  TODO        AlarmManager alarmManager = (AlarmManager)SpringBeanHelper.getBean("alarmManager");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", monitorResult);
//    FIXME  TODO        alarmManager.createAlarm(bussNo,monitorName,monitorDesc,Integer.valueOf(maxRetryTimes),Integer.valueOf(retryPeriod),orgs,Long.valueOf(templateId),jsonObject);
        }

        LOG.info("end Job [ " + scheduleJob.getJobName() + " ]");
    }
}
package com.yxl.demo.quartzProject.biz.manager.quartz;

import com.yxl.demo.quartzProject.common.YOrNEnum;
import com.yxl.demo.quartzProject.dataobject.BaseDO;
import com.yxl.demo.quartzProject.dataobject.ScheduleJob;
import com.yxl.demo.quartzProject.util.PropertyUtil;
import com.yxl.demo.quartzProject.util.SpringBeanHelper;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 动态装载 监控项Job
 * author: xiaolong.yuanxl
 * date: 2015-06-08 下午4:29
 */
public class LoadMonitorItemJob implements Serializable, Job {

    private static final Logger LOG = LoggerFactory.getLogger(LoadMonitorItemJob.class);

    private static final long serialVersionUID = -2L; //固定编号,以免自动生成quartz跟数据库对比产生不一致

    private static boolean isStartup = true;

    private static final String QUARTZ_JOB_GROUP_NAME = "default-DW-Group-dynamic";

//    private static final String JOB_DISABLE = "0";
//    private static final String JOB_ENABLE = "1";
//    private static final String JOB_DELETE = "2";

    @Override
    public void execute(JobExecutionContext context) throws RuntimeException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()) + "I'm enter loadMonitorItemJob");

        try {

            if (LOG.isDebugEnabled()) {
                LOG.debug(sdf.format(new Date()) + " ------> start load quartz job");
            }
            List<MonitorItemDO> needReloadList = getNeedReloadItem();
            List<ScheduleJob> list = toScheduleJob(needReloadList);
            //1.reload
            reload(list);
            //手动获取bean
//    FIXME TODO        MonitorItemDAO monitorItemDAO = (MonitorItemDAO) SpringBeanHelper.getBean("monitorItemDAO");
            for (MonitorItemDO itemDO : needReloadList) {
                //2.update
                itemDO.setIsNeedReload(YOrNEnum.N.getCode());
//    FIXME TODO            monitorItemDAO.updateMonitorItem(itemDO);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(sdf.format(new Date()) + " -------> end load quartz job");
            }

        } catch (SchedulerException se) {
            LOG.error("execute LoadMonitorItemJob happend error, ", se);
            throw new RuntimeException("critical error happend process need be stop ", se);
        }
    }


    //获取需要加载的监控项
    private List<MonitorItemDO> getNeedReloadItem() throws SchedulerException {
        //手动获取bean
//        MonitorItemDAO monitorItemDAO = (MonitorItemDAO) SpringBeanHelper.getBean("monitorItemDAO");

        List<MonitorItemDO> ret = new ArrayList<MonitorItemDO>();
        if (isStartup) {
            LOG.info("loadMonitorItemJob started , to delete all exist dynamic job");
            Scheduler scheduler = (Scheduler) SpringBeanHelper.getBean("scheduler");
            List<String> groups = scheduler.getJobGroupNames();
            if (groups != null && !groups.isEmpty()) {
                // 删除动态Jobs
                Set<JobKey> keys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(QUARTZ_JOB_GROUP_NAME));
                for (JobKey key : keys) {
                    scheduler.deleteJob(key);
                }
                // 匹配并删除 静态Jobs
                RuntimeJobManager runtimeJobManager = (RuntimeJobManager) SpringBeanHelper.getBean("runtimeJobManager");
                List<String> existJobNames = runtimeJobManager.getRunningJobs();
                Set<JobKey> dbKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(PropertyUtil.getInstance().getProperty("quartz.job.group")));
                for (JobKey dbKey : dbKeys) {
                    // 如果 DB比 xml 多则删除DB的
                    if (!existJobNames.contains(dbKey.getName())) {
                        scheduler.deleteJob(dbKey);
                    }
                }
            }
// FIXME TODO  ret.addAll(monitorItemDAO.selectListWithoutEnable());
            isStartup = false;
        } else {
//  FIXME TODO  ret.addAll(monitorItemDAO.selectNeedReloadList());
        }
        return ret;
    }

    // 转换成封装体
    private List<ScheduleJob> toScheduleJob(List<MonitorItemDO> list) {
        List<ScheduleJob> ret = new ArrayList<ScheduleJob>();
        for (MonitorItemDO monitorItemDO : list) {
            ScheduleJob job = new ScheduleJob();
            job.setJobId("monitorItem-" + monitorItemDO.getId());   //e.g: monitorItem-1000
            job.setBussNo(monitorItemDO.getBussNo());
            job.setJobName(String.valueOf(monitorItemDO.getId()));//防止monitorItem名称改了后找不到对应的quartzJob
            job.setJobGroup(QUARTZ_JOB_GROUP_NAME);
            job.setJobStatus(monitorItemDO.getIsEnable());
            job.setCronExpression(monitorItemDO.getItemCron());
            job.setDesc(monitorItemDO.getItemDesc());
            job.setMonitorType(monitorItemDO.getItemType());
            job.setOrgs(monitorItemDO.getOrgs());
            job.setExecContent(monitorItemDO.getExecContent());
            job.setTemplateId(monitorItemDO.getTempateId() == null ? "" : String.valueOf(monitorItemDO.getTempateId()));
            job.setMaxRetryTimes(String.valueOf(monitorItemDO.getAlarmMaxRetryTimes()));
            job.setRetryPeriod(String.valueOf(monitorItemDO.getAlarmRetryPeriod()));

            ret.add(job);
        }
        return ret;
    }


    private void reload(List<ScheduleJob> jobList) {
        // 手工获取bean
        Scheduler scheduler = (Scheduler) SpringBeanHelper.getBean("scheduler");
        for (ScheduleJob job : jobList) {
            try {
                TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

                //获取trigger
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

                //删除所有job
                deleteJob(job);
                //再添加 Job是 enable的,再添加
                if (StringUtils.equalsIgnoreCase(YOrNEnum.Y.getCode(), job.getJobStatus())) {
                    newJobWithRun(trigger, job, scheduler);
                }
            } catch (Exception e) {
                LOG.error("load quartz job happend error: ", e);
            }
        }
    }

    private void newJobWithRun(CronTrigger trigger, ScheduleJob job, Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
                .withIdentity(job.getJobName(), job.getJobGroup()).build();
        //放置封装体
        jobDetail.getJobDataMap().put("scheduleJob", job);

        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
                .getCronExpression());

        //按新的cronExpression表达式构建一个新的trigger
        trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void pauseJob(ScheduleJob scheduleJob) throws Exception {
        // 手工获取bean
        Scheduler scheduler = (Scheduler) SpringBeanHelper.getBean("scheduler");
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    public void resumeJob(ScheduleJob scheduleJob) throws Exception {
        // 手工获取bean
        Scheduler scheduler = (Scheduler) SpringBeanHelper.getBean("scheduler");
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    public void deleteJob(ScheduleJob scheduleJob) throws Exception {
        // 手工获取bean
        Scheduler scheduler = (Scheduler) SpringBeanHelper.getBean("scheduler");
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.deleteJob(jobKey);
    }

    public void runJob(ScheduleJob scheduleJob) throws Exception {
        // 手工获取bean
        Scheduler scheduler = (Scheduler) SpringBeanHelper.getBean("scheduler");
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    public void modifyJobTime(ScheduleJob scheduleJob) throws Exception {
        // 手工获取bean
        Scheduler scheduler = (Scheduler) SpringBeanHelper.getBean("scheduler");
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        //获取trigger
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        //按新的cronExpression表达式重新构建trigger
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        //按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    private class MonitorItemDO extends BaseDO {

        private String bussNo;    //监控业务

        private String itemName;    //监控项名称

        private String itemDesc;    //监控项描述

        private String itemCron;    //监控项频率，cron表达式

        private String itemType;    //类型 shell , sql , sys

        private String orgs;    //人员组,逗号分隔

        private String execContent; //执行元信息，json格式，针对shell 和 sql 类型

        private String isEnable;    //是否启用 Y-启用 N-禁用

        private String isNeedReload;    //是否需要重载 Y-重载 N-不重载

        private Integer alarmMaxRetryTimes = 3;  //报警最大重试次数

        private Integer alarmRetryPeriod = 3;    //报警重试间隔 单位分钟

        private Long tempateId; //报警模板ID

        public String getBussNo() {
            return bussNo;
        }

        public void setBussNo(String bussNo) {
            this.bussNo = bussNo;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemDesc() {
            return itemDesc;
        }

        public void setItemDesc(String itemDesc) {
            this.itemDesc = itemDesc;
        }

        public String getItemCron() {
            return itemCron;
        }

        public void setItemCron(String itemCron) {
            this.itemCron = itemCron;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getOrgs() {
            return orgs;
        }

        public void setOrgs(String orgs) {
            this.orgs = orgs;
        }

        public String getExecContent() {
            return execContent;
        }

        public void setExecContent(String execContent) {
            this.execContent = execContent;
        }

        public String getIsEnable() {
            return isEnable;
        }

        public void setIsEnable(String isEnable) {
            this.isEnable = isEnable;
        }

        public String getIsNeedReload() {
            return isNeedReload;
        }

        public void setIsNeedReload(String isNeedReload) {
            this.isNeedReload = isNeedReload;
        }

        public Integer getAlarmMaxRetryTimes() {
            return alarmMaxRetryTimes;
        }

        public void setAlarmMaxRetryTimes(Integer alarmMaxRetryTimes) {
            this.alarmMaxRetryTimes = alarmMaxRetryTimes;
        }

        public Integer getAlarmRetryPeriod() {
            return alarmRetryPeriod;
        }

        public void setAlarmRetryPeriod(Integer alarmRetryPeriod) {
            this.alarmRetryPeriod = alarmRetryPeriod;
        }

        public Long getTempateId() {
            return tempateId;
        }

        public void setTempateId(Long tempateId) {
            this.tempateId = tempateId;
        }
    }


}

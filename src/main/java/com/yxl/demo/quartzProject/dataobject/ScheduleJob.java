package com.yxl.demo.quartzProject.dataobject;

import java.io.Serializable;

/**
 * 通用Job抽象
 * Created by huanglichao on 15/6/3.
 */
public class ScheduleJob implements Serializable{

    private static final long serialVersionUID = -4L; //固定编号,以免自动生成quartz跟数据库对比产生不一致

    /** 任务id */
    private String jobId;

    /** bussNo */
    private String bussNo;

    /** 任务名称 */
    private String jobName;

    /** 任务分组 */
    private String jobGroup;

    /** 任务状态 0禁用 1启用 2删除*/
    private String jobStatus;

    /** 任务运行时间表达式 */
    private String cronExpression;

    /** 任务描述 */
    private String desc;

    /** 监控类型 */
    private String monitorType;

    /** 监控人员组 */
    private String orgs;

    /** 执行信息 */
    private String execContent;

    /** 模板 */
    private String templateId;

    /** 最大重试次数 */
    private String maxRetryTimes;

    /** 重试间隔 分钟*/
    private String retryPeriod;


    public String getBussNo() {
        return bussNo;
    }

    public void setBussNo(String bussNo) {
        this.bussNo = bussNo;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }


    public String getMaxRetryTimes() {
        return maxRetryTimes;
    }

    public void setMaxRetryTimes(String maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    public String getRetryPeriod() {
        return retryPeriod;
    }

    public void setRetryPeriod(String retryPeriod) {
        this.retryPeriod = retryPeriod;
    }
}
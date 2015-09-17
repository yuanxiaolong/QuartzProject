package com.yxl.demo.quartzProject.biz.manager.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.Serializable;

/**
 * 抽象发送者
 * author: xiaolong.yuanxl
 * date: 2015-06-08 下午2:12
 */
public abstract class AbstarctSenderJob implements Serializable, Job {

    private static final long serialVersionUID = -1L; //固定编号,以免自动生成quartz跟数据库对比产生不一致

//    //这里spring注入不进去,需要手动生成
//    protected static McMsgDAO mcMsgDAO;
//
//    static {
//        mcMsgDAO = (McMsgDAO) SpringBeanHelper.getBean("mcMsgDAO");
//    }

    //类型
    protected abstract String type();

    //执行逻辑,不要加事务,每条消息发送互不干扰
    protected abstract void doLogic();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        doLogic();
    }

}

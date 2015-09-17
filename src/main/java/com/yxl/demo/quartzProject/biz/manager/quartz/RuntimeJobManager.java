package com.yxl.demo.quartzProject.biz.manager.quartz;

import java.util.ArrayList;
import java.util.List;

/**
 * 运行中的job管理
 * author: xiaolong.yuanxl
 * date: 2015-06-09 下午6:52
 */
public class RuntimeJobManager {

    //运行中的Jobs,便于匹配清理
    private List<String> runningJobs = new ArrayList<String>();


    public List<String> getRunningJobs() {
        return runningJobs;
    }

    public void setRunningJobs(List<String> runningJobs) {
        this.runningJobs = runningJobs;
    }
}

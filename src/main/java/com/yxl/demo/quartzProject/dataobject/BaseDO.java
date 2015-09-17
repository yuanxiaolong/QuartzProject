package com.yxl.demo.quartzProject.dataobject;

/**
 * 共有字段类
 * author: xiaolong.yuanxl
 * date: 2015-06-03 下午4:29
 */
public abstract class BaseDO {

    protected Long id;
    protected String gmtCreated;
    protected String gmtModified;
    protected String gmtCreator;
    protected String gmtModifier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(String gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getGmtCreator() {
        return gmtCreator;
    }

    public void setGmtCreator(String gmtCreator) {
        this.gmtCreator = gmtCreator;
    }

    public String getGmtModifier() {
        return gmtModifier;
    }

    public void setGmtModifier(String gmtModifier) {
        this.gmtModifier = gmtModifier;
    }

}

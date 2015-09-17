package com.yxl.demo.quartzProject.common;

import org.apache.commons.lang.StringUtils;

/**
 * 监控项类型枚举
 *
 * author: xiaolong.yuanxl
 * date: 2015-05-01 下午11:14
 */
public enum MonitorItemTypeEnum {

    SHELL("shell","shell"),
    SYS("sys","sys")
    ;

    private String code;

    private String desc;

    MonitorItemTypeEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static MonitorItemTypeEnum match(String type){
        for (MonitorItemTypeEnum value : values()) {
            if (StringUtils.equalsIgnoreCase(type, value.getCode())) {
                return value;
            }
        }
        return null;
    }


}

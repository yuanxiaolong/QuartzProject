package com.yxl.demo.quartzProject.common;

import org.apache.commons.lang.StringUtils;

/**
 * 发送类型
 *
 * author: xiaolong.yuanxl
 * date: 2015-05-01 下午11:14
 */
public enum McSendChannelEnum {

    EMAIL("email","email"),
    SMS("sms","短信")
    ;

    private String code;

    private String desc;

    McSendChannelEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static McSendChannelEnum match(String type){
        for (McSendChannelEnum value : values()) {
            if (StringUtils.equalsIgnoreCase(type, value.getCode())) {
                return value;
            }
        }
        return null;
    }


}

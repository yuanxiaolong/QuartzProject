package com.yxl.demo.quartzProject.common;

import org.apache.commons.lang.StringUtils;

/**
 * 发送结果
 *
 * author: xiaolong.yuanxl
 * date: 2015-05-01 下午11:14
 */
public enum McSendResultEnum {

    成功("success","成功"),
    失败("fail","失败"),
    错误("error","错误"),
    重试("retry","重试"),
    未发送("nosend","未发送"),
    ;

    private String code;

    private String desc;

    McSendResultEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static McSendResultEnum match(String type){
        for (McSendResultEnum value : values()) {
            if (StringUtils.equalsIgnoreCase(type, value.getCode())) {
                return value;
            }
        }
        return null;
    }


}

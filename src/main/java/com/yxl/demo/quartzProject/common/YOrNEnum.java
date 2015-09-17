package com.yxl.demo.quartzProject.common;

import org.apache.commons.lang.StringUtils;

/**
 * Y or N
 *
 * author: xiaolong.yuanxl
 * date: 2015-05-01 下午11:14
 */
public enum YOrNEnum {

    Y("Y","Y"),
    N("N","N")
    ;

    private String code;

    private String desc;

    YOrNEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static YOrNEnum match(String type){
        for (YOrNEnum value : values()) {
            if (StringUtils.equalsIgnoreCase(type, value.getCode())) {
                return value;
            }
        }
        return null;
    }


}

package com.yxl.demo.quartzProject.common;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信封装
 *
 * author: xiaolong.yuanxl
 * date: 2015-09-16 上午10:48
 */
public class SMSModelDTO {

    private String phoneNum;    //电话号

    private String content;     //内容

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //不用 this. 了还可以暴露方法共用

    public static boolean isPhone(String mobiles){
        if (StringUtils.isBlank(mobiles)){
            return false;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isContentNoTooLong(String content){
        return StringUtils.isNotBlank(content) && content.length() < 200;
    }


    public static void main(String[] args) {
        System.out.println(isPhone("18101819963"));
        System.out.println("您本次获取的随机验证码是：439388，请尽快返回完成验证。如非本人使用，敬请忽略本信息。本条免信息费，不含通信费【芒果tv】".length());
    }


}

package com.yxl.demo.quartzProject.common;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮件封装对象
 * author: xiaolong.yuanxl
 * date: 2015-06-08 上午11:29
 */
public class EmailModelDTO {

    private String to;  // 收件人
    private String subject; // 主题
    private String content; // 内容

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static boolean isEmail(String emailAddr){
        if (StringUtils.isBlank(emailAddr)){
            return false;
        }
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(emailAddr);
        return matcher.matches();
    }
}

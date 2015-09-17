package com.yxl.demo.quartzProject.biz.manager;



import com.yxl.demo.quartzProject.common.EmailModelDTO;

import java.util.Map;

/**
 * 邮件发送
 * author: xiaolong.yuanxl
 * date: 2015-06-08 上午11:17
 */
public interface MailSenderManager {


    /**
     * 发送velocity格式的邮件
     * @param emailModelDTO 邮件体
     * @param velocityParamMap 包含所有velocity变量的map
     * @return 发送结果
     */
    boolean sendWithTemplate(EmailModelDTO emailModelDTO, Map<String, Object> velocityParamMap);

}

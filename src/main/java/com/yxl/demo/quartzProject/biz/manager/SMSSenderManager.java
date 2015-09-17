package com.yxl.demo.quartzProject.biz.manager;



import com.yxl.demo.quartzProject.common.SMSModelDTO;

import java.util.Map;

/**
 * 短信发送接口
 *
 * author: xiaolong.yuanxl
 * date: 2015-09-16 上午11:06
 */
public interface SMSSenderManager {

    boolean sendWithTemplate(SMSModelDTO smsModelDTO, Map<String, Object> velocityParamMap);

}

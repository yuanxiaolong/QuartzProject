package com.yxl.demo.quartzProject.biz.manager;


import com.yxl.demo.quartzProject.common.SMSModelDTO;
import com.yxl.demo.quartzProject.util.VelocityHelpler;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 短信发送调用http服务
 *
 * author: xiaolong.yuanxl
 * date: 2015-09-16 上午11:07
 */
public class SMSSenderManagerImpl implements SMSSenderManager {

    private static final Logger LOG = LoggerFactory.getLogger(SMSSenderManagerImpl.class);

    /**
     * 系统sms账户名称，申请获取
     */
    private final static String SMS_APPID = "1";

    /**
     * 系统sms密码，经过MD5加密后的密码（32位长度，16进制，字母大写）
     */
    private final static String SMS_PWD = "2";

    /**
     * MD5密码
     */
    private final static String SMS_PWD_MD5 = "3";

    /**
     * 虚似IP
     */
    private final static String VIP = "192.168.1.1";

    /**
     * 短信报警的URL
     */
    private final static String SEND_URL = "http://192.168.1.168:80/sms/sendMsg";



    public boolean sendWithTemplate(SMSModelDTO smsModelDTO,Map<String,Object> velocityParamMap){
        if (smsModelDTO == null || StringUtils.isBlank(smsModelDTO.getPhoneNum()) || StringUtils.isBlank(smsModelDTO.getContent())){
            LOG.warn("send sms but smsModel is null ");
            return false;
        }
        String phoneNumber = smsModelDTO.getPhoneNum();
        String content = smsModelDTO.getContent();
        if (!SMSModelDTO.isPhone(phoneNumber)){
            LOG.warn("send sms but phoneNum is invalid ");
            return false;
        }
        if (!SMSModelDTO.isContentNoTooLong(content)) {
            LOG.warn("send sms but content is exceed 50 chars ");
            return false;
        }

        try{
            VelocityHelpler helpler = VelocityHelpler.getInstance();
            String text = helpler.merge(smsModelDTO.getContent(), velocityParamMap);//渲染后的模板
            if (LOG.isDebugEnabled()){
                LOG.debug("send email content: " + text);
            }

            HttpPost httpRequest = new HttpPost(SEND_URL);
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("SMS_appid", SMS_APPID));
            params.add(new BasicNameValuePair("SMS_pwd", SMS_PWD_MD5));
            params.add(new BasicNameValuePair("SMS_content", text));
            params.add(new BasicNameValuePair("SMS_mobilenum", phoneNumber));
            params.add(new BasicNameValuePair("SMS_time", "0"));
            params.add(new BasicNameValuePair("SMS_IP", VIP));
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse != null) {
                String responseStr = EntityUtils.toString(httpResponse.getEntity());
                LOG.info(" call send sms response is : " + responseStr);
            }
        }catch (Exception e){
            LOG.error("send sms happend error, phoneNum: " + phoneNumber + " content: " + content);
            return false;
        }
        LOG.info("send sms to " + phoneNumber + " success!");
        return true;
    }

//    public static void main(String[] args) throws Exception{
//        HttpPost httpRequest = new HttpPost(SEND_URL);
//        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//        params.add(new BasicNameValuePair("SMS_appid", SMS_APPID));
//        params.add(new BasicNameValuePair("SMS_pwd", SMS_PWD_MD5));
//        params.add(new BasicNameValuePair("SMS_content", "这是 1条 测试数据 hello world"));
//        params.add(new BasicNameValuePair("SMS_mobilenum", "15274870080"));
//        params.add(new BasicNameValuePair("SMS_time", "0"));
//        params.add(new BasicNameValuePair("SMS_IP", VIP));
//        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
//        if (httpResponse != null) {
//            String responseStr = EntityUtils.toString(httpResponse.getEntity());
//            System.out.println(" call send sms response is : " + responseStr);
//        }
//    }


}

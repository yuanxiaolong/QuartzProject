package com.yxl.demo.quartzProject.biz.manager;


import com.yxl.demo.quartzProject.common.EmailModelDTO;
import com.yxl.demo.quartzProject.util.HttpClientHelper;
import com.yxl.demo.quartzProject.util.PropertyUtil;
import com.yxl.demo.quartzProject.util.VelocityHelpler;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * author: xiaolong.yuanxl
 * date: 2015-06-08 上午11:18
 */
public class MailSenderManagerImpl implements MailSenderManager {

    private static final Logger LOG = LoggerFactory.getLogger(MailSenderManagerImpl.class);

    private static String MAIL_SERVICE_HOST = PropertyUtil.getInstance().getProperty("mail.service.host");

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private SimpleMailMessage simpleMailMessage;

    @Override
    public boolean sendWithTemplate(EmailModelDTO emailModelDTO, Map<String, Object> velocityParamMap) {
        if (emailModelDTO == null) {
            LOG.warn("sendWithTemplate illegal args emailModelDTO is null");
            return false;
        }
        if (!EmailModelDTO.isEmail(emailModelDTO.getTo())){
            LOG.warn("send mail but emailAddr is invalid");
            return false;
        }
        return  sendBySmtp(emailModelDTO, velocityParamMap);
    }

    private boolean sendBySmtp(EmailModelDTO emailModelDTO, Map<String, Object> velocityParamMap){
        // smtp 服务器发送
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(simpleMailMessage.getFrom());//发送人,从配置文件中取得
            messageHelper.setTo(emailModelDTO.getTo());//接收人
            messageHelper.setSubject(emailModelDTO.getSubject());// 主题
            VelocityHelpler helpler = VelocityHelpler.getInstance();
            String text = helpler.merge(emailModelDTO.getContent(), velocityParamMap);//渲染后的模板
            if (LOG.isDebugEnabled()){
                LOG.debug("send email content: " + text);
            }
            messageHelper.setText(text, true);   //use html
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            LOG.error("sendWithTemplate happend error ", e);
            return false;
        }

        LOG.info("send email to " + emailModelDTO.getTo() + " success!");
        return true;
    }


    private boolean sendByHttpService(EmailModelDTO emailModelDTO, Map<String, Object> velocityParamMap){
        //端口80固定写死
        HttpClientHelper httpClientHelper = new HttpClientHelper(StringUtils.isBlank(MAIL_SERVICE_HOST)?"10.100.4.245":MAIL_SERVICE_HOST,80);

        VelocityHelpler helpler = VelocityHelpler.getInstance();
        String text = helpler.merge(emailModelDTO.getContent(), velocityParamMap);//渲染后的模板
        if (LOG.isDebugEnabled()){
            LOG.debug("send email content: " + text);
        }

        //敏感信息写死在代码里
        Map<String,String> param = new HashMap<String, String>();
        param.put("SES_appid","1");
        param.put("SES_pwd","2");
        param.put("SES_fromName","3");
        param.put("SES_fromAddress","4@126");

        param.put("SES_title",emailModelDTO.getSubject());
        param.put("SES_content",text);
        param.put("SES_address",emailModelDTO.getTo());
        try {
            HttpResponse response = httpClientHelper.exec(HttpClientHelper.HttpMethod.POST, "/ses/sendEmail", param);
            String responseString = toString(response.getEntity().getContent());
            LOG.info("response: " + responseString);

            JSONObject jsonObject = JSONObject.fromObject(responseString);
            if (jsonObject.getInt("code") != 0){
                return false;
            }

        }catch (Exception e){
            LOG.error("send email to "+ emailModelDTO.getTo() + " fail!");
            return false;
        }
        LOG.info("send email to " + emailModelDTO.getTo() + " success!");
        return true;
    }


    private static String toString(InputStream in)
            throws UnsupportedEncodingException, IOException {
        StringWriter writer = new StringWriter();
        copyLarge(new InputStreamReader(in, "utf-8"), writer);
        return writer.toString();
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static long copyLarge(Reader input, Writer output)
            throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

//    public static void main(String[] args) {
//
//        String template = "${owner}：您的${type} : ${bill} 在 ${date} 日已支付成功$!{aa}";
//        Map<String,Object> context = new HashMap<String, Object>();
//        context.put("owner", "nassir");
//        context.put("bill", "201203221000029763");
//        context.put("type", "订单");
//        context.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//
//        VelocityHelpler helpler = VelocityHelpler.getInstance();
//        System.out.println(helpler.merge(template, context));
//
//    }


    // 提供SET 方法 以便注入
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }

}

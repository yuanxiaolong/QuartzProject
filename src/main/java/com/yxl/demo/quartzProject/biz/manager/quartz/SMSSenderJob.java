package com.yxl.demo.quartzProject.biz.manager.quartz;


import com.yxl.demo.quartzProject.biz.manager.SMSSenderManager;
import com.yxl.demo.quartzProject.common.McSendChannelEnum;
import com.yxl.demo.quartzProject.common.McSendResultEnum;
import com.yxl.demo.quartzProject.common.SMSModelDTO;
import com.yxl.demo.quartzProject.dataobject.BaseDO;
import com.yxl.demo.quartzProject.util.SpringBeanHelper;
import com.yxl.demo.quartzProject.common.YOrNEnum;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 发送短信job
 *
 * author: xiaolong.yuanxl
 * date: 2015-09-16 上午10:41
 */
public class SMSSenderJob extends AbstarctSenderJob {

    private static final long serialVersionUID = -7L; //固定编号,以免自动生成quartz跟数据库对比产生不一致

    private static final Logger LOG = LoggerFactory.getLogger(MailSenderJob.class);

    @Override
    protected String type() {
        return McSendChannelEnum.SMS.getCode();
    }

    @Override
    protected void doLogic() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()) + "I'm enter the SMSSenderJob");

        //1.根据类型获取 mc_msg 表里没有发送的邮件
//  FIXME TODO      McMsgDAO mcMsgDAO = (McMsgDAO) SpringBeanHelper.getBean("mcMsgDAO");
        SMSSenderManager smsSenderManager = (SMSSenderManager) SpringBeanHelper.getBean("smsSenderManager");


        List<McMsgDO> msgs = new ArrayList<McMsgDO>(); // FIXME TODO mcMsgDAO.selectSendList(type());
        //2.遍历发送
        for (McMsgDO msg : msgs) {
            try{
                //如果 重试 没到下次发送时间，则跳过

                if (StringUtils.isNotBlank(msg.getNextRetryDatetime())){

                    Calendar now = Calendar.getInstance();
                    now.setTime(new Date());

                    Calendar next = Calendar.getInstance();
                    next.setTime(sdf.parse(msg.getNextRetryDatetime()));

                    if (StringUtils.equalsIgnoreCase(McSendResultEnum.重试.getCode(),msg.getSendResult())
                            && (now.before(next))){
                        continue;
                    }
                }


                SMSModelDTO dto = new SMSModelDTO();
                dto.setPhoneNum(msg.getReceivedUser()); //收信人
                dto.setContent(msg.getMsgContent()); //内容

                //3.发送
                boolean result = smsSenderManager.sendWithTemplate(dto, msg.toVelocityParamMap());
                Integer alreadyRetry = msg.getAlreadyRetryTimes() == null ? 0 : msg.getAlreadyRetryTimes();

                if (result){
                    msg.setSendTime(sdf.format(new Date())); //sendTime
                    msg.setSendResult(McSendResultEnum.成功.getCode()); // sendResult
                    msg.setIsSend(YOrNEnum.Y.getCode());
                }else if (alreadyRetry + 1 < msg.getMaxRetryTimes()){
                    msg.setSendResult(McSendResultEnum.重试.getCode());
                    msg.setAlreadyRetryTimes(alreadyRetry + 1); // retry + 1
                    //以本次发送后时间为基准,后移分钟
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.MINUTE,msg.getRetryPeriod() == null || msg.getRetryPeriod().intValue() < 0 ? 0 : msg.getRetryPeriod());
                    msg.setNextRetryDatetime(sdf.format(calendar.getTime()));   // nextRetry
                }else{
                    msg.setSendResult(McSendResultEnum.重试.getCode());
                    msg.setAlreadyRetryTimes(alreadyRetry + 1); // retry + 1
                    msg.setIsSend(YOrNEnum.Y.getCode());    //防止无限重试
                }
//    FIXME TODO            mcMsgDAO.updateMsg(msg); // update
            }catch (Exception e){
                LOG.error("send sms happend error ",e);
                msg.setSendResult(McSendResultEnum.错误.getCode()); // sendResult
                msg.setIsSend(YOrNEnum.Y.getCode());    //防止无限重试
//     FIXME TODO           mcMsgDAO.updateMsg(msg); // update
            }

        }



    }


    private class McMsgDO extends BaseDO {


        private Long templateId;    //模板ID

        private String msgName; //消息名称,快照 template_name

        private String msgDesc; //消息描述,快照

        private String msgTitle;    //消息标题,快照

        private String msgContent;  //消息内容已拼接,快照

        private String isSend;  //是否已发送

        private String sendResult;  // 发送结果 success, fail ，error，retry

        private String sendChannel; // 发送渠道 email sms

        private String sendTime;    // 已发送时刻

        private String receivedUser;    // 接收者邮件或手机

        private Integer maxRetryTimes;    // 最大重试次数（冗余，从报警中平移）

        private Integer retryPeriod; // 重试间隔（冗余，从报警中平移）

        private Integer alreadyRetryTimes;   // 重试次数,正常发送时，重试为0

        private String nextRetryDatetime;   // 下次重试时间 yyyy-MM-dd HH:mm:ss

        private String extParam;    // 扩展参数 json

        public Long getTemplateId() {
            return templateId;
        }

        public void setTemplateId(Long templateId) {
            this.templateId = templateId;
        }

        public String getMsgName() {
            return msgName;
        }

        public void setMsgName(String msgName) {
            this.msgName = msgName;
        }

        public String getMsgDesc() {
            return msgDesc;
        }

        public void setMsgDesc(String msgDesc) {
            this.msgDesc = msgDesc;
        }

        public String getMsgTitle() {
            return msgTitle;
        }

        public void setMsgTitle(String msgTitle) {
            this.msgTitle = msgTitle;
        }

        public String getMsgContent() {
            return msgContent;
        }

        public void setMsgContent(String msgContent) {
            this.msgContent = msgContent;
        }

        public String getIsSend() {
            return isSend;
        }

        public void setIsSend(String isSend) {
            this.isSend = isSend;
        }

        public String getSendResult() {
            return sendResult;
        }

        public void setSendResult(String sendResult) {
            this.sendResult = sendResult;
        }

        public String getSendChannel() {
            return sendChannel;
        }

        public void setSendChannel(String sendChannel) {
            this.sendChannel = sendChannel;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getReceivedUser() {
            return receivedUser;
        }

        public void setReceivedUser(String receivedUser) {
            this.receivedUser = receivedUser;
        }

        public Integer getMaxRetryTimes() {
            return maxRetryTimes;
        }

        public void setMaxRetryTimes(Integer maxRetryTimes) {
            this.maxRetryTimes = maxRetryTimes;
        }

        public Integer getRetryPeriod() {
            return retryPeriod;
        }

        public void setRetryPeriod(Integer retryPeriod) {
            this.retryPeriod = retryPeriod;
        }

        public Integer getAlreadyRetryTimes() {
            return alreadyRetryTimes;
        }

        public void setAlreadyRetryTimes(Integer alreadyRetryTimes) {
            this.alreadyRetryTimes = alreadyRetryTimes;
        }

        public String getNextRetryDatetime() {
            return nextRetryDatetime;
        }

        public void setNextRetryDatetime(String nextRetryDatetime) {
            this.nextRetryDatetime = nextRetryDatetime;
        }

        public String getExtParam() {
            return extParam;
        }

        public void setExtParam(String extParam) {
            this.extParam = extParam;
        }

        //构造velocity参数
        public Map<String, Object> toVelocityParamMap() {
            Map<String, Object> map = new HashMap<String, Object>();
            try {
                JSONObject jsonObject = JSONObject.fromObject(extParam);
                Set<String> set = (Set<String>) jsonObject.keySet();
                for (String key : set) {
                    try {
                        Object val = jsonObject.get(key);
                        if (val instanceof JSONArray) {
                            JSONArray array = JSONArray.fromObject(val);
                            map.put(key, array.toArray());
                        } else {
                            map.put(key, val);
                        }
                    } catch (Exception e1) {
                        continue;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return map;
        }
    }
}

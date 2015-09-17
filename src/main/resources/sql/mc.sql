-- also in db vayne

CREATE TABLE `mc_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_created` varchar(32) DEFAULT '',
  `gmt_modified` varchar(32) DEFAULT '',
  `gmt_creator` varchar(64) DEFAULT '',
  `gmt_modifier` varchar(64) DEFAULT '',
  `template_name` varchar(128) DEFAULT '' COMMENT '模板名称 唯一索引',
  `template_desc` varchar(2048) DEFAULT '' COMMENT '模板描述',
  `template_title` varchar(512) DEFAULT '' COMMENT '标题',
  `template_content` varchar(8192) DEFAULT '' COMMENT '模板内容',
  `send_channel` enum('email','sms') NOT NULL DEFAULT 'email' COMMENT '发送渠道 email-邮件 sms-短信',
  `send_type` enum('realtime','fixtime') NOT NULL DEFAULT 'realtime' COMMENT '发送类型  realtime-实时  fixtime-定时',
  `fix_time` varchar(64) DEFAULT '' COMMENT '定时HH:mm',
  `is_enable` char(1) DEFAULT NULL COMMENT '是否启用 Y-启用 N-禁用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_t_name` (`template_name`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='消息模板表';

CREATE TABLE `mc_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_created` varchar(32) DEFAULT '',
  `gmt_modified` varchar(32) DEFAULT '',
  `gmt_creator` varchar(64) DEFAULT '',
  `gmt_modifier` varchar(64) DEFAULT '',
  `template_id` bigint(20) COMMENT '模板ID',
  `msg_name` varchar(128) DEFAULT '' COMMENT '消息名称,快照 template_name',
  `msg_desc` varchar(2048) DEFAULT '' COMMENT '消息描述,快照 template_desc',
  `msg_title` varchar(512) DEFAULT '' COMMENT '消息标题,快照 template_title',
  `msg_content` varchar(8192) DEFAULT '' COMMENT '消息内容已拼接,快照 template_content',
  `is_send` char(1) DEFAULT 'N' COMMENT '是否已发送 Y-已发送过 N-未发送过',
  `send_result` enum('success','fail','error','retry','nosend') NOT NULL DEFAULT 'nosend' COMMENT 'nosend 未发送,发送结果 success, fail ，error，retry',
  `send_channel` enum('email','sms') NOT NULL DEFAULT 'email' COMMENT '发送渠道 email-邮件 sms-短信',
  `send_time` varchar(64) DEFAULT '' COMMENT '已发送时刻',
  `received_user` varchar(64) DEFAULT '' COMMENT '接收者邮件或手机',
  `max_retry_times` smallint(6) DEFAULT '3' COMMENT '最大重试次数 冗余，从报警中平移',
  `retry_period` smallint(6) DEFAULT '3' COMMENT '重试间隔 单位分钟 冗余，从报警中平移',
  `already_retry_times` smallint(6) DEFAULT '0' COMMENT '重试次数,正常发送时，重试为0',
  `next_retry_datetime` varchar(64) DEFAULT '' COMMENT '下次重试时间 yyyy-MM-dd HH:mm:ss',
  `ext_param` varchar(4096) DEFAULT NULL COMMENT '扩展参数 json ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='消息表';


CREATE TABLE `mc_msg_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_created` varchar(32) DEFAULT '',
  `gmt_modified` varchar(32) DEFAULT '',
  `gmt_creator` varchar(64) DEFAULT '',
  `gmt_modifier` varchar(64) DEFAULT '',
  `template_id` bigint(20) COMMENT '模板ID',
  `msg_name` varchar(128) DEFAULT '' COMMENT '消息名称,快照 template_name',
  `msg_desc` varchar(2048) DEFAULT '' COMMENT '消息描述,快照 template_desc',
  `msg_title` varchar(512) DEFAULT '' COMMENT '消息标题,快照 template_title',
  `msg_content` varchar(8192) DEFAULT '' COMMENT '消息内容已拼接,快照 template_content',
  `is_send` char(1) DEFAULT 'N' COMMENT '是否已发送 Y-已发送过 N-未发送过',
  `send_result` enum('success','fail','error','retry') NOT NULL DEFAULT 'retry' COMMENT '发送结果 success, fail ，error，retry',
  `send_channel` enum('email','sms') NOT NULL DEFAULT 'email' COMMENT '发送渠道 email-邮件 sms-短信',
  `send_time` varchar(64) DEFAULT '' COMMENT '已发送时刻',
  `received_user` varchar(64) DEFAULT '' COMMENT '接收者邮件或手机',
  `max_retry_times` smallint(6) DEFAULT '3' COMMENT '最大重试次数 冗余，从报警中平移',
  `retry_period` smallint(6) DEFAULT '3' COMMENT '重试间隔 单位分钟 冗余，从报警中平移',
  `already_retry_times` smallint(6) DEFAULT '0' COMMENT '重试次数,正常发送时，重试为0',
  `next_retry_datetime` varchar(64) DEFAULT '' COMMENT '下次重试时间 yyyy-MM-dd HH:mm:ss',
  `ext_param` varchar(4096) DEFAULT NULL COMMENT '扩展参数 json ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='历史消息表';

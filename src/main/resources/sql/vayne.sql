CREATE DATABASE if not exists vayne DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

grant all privileges on vayne.* to 'vayne'@'%' identified by 'vayne1234';
grant all privileges on vayne.* to 'vayne'@'localhost' identified by 'vayne1234';


use vayne;

-- table

CREATE TABLE `monitor_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_created` varchar(32) DEFAULT '',
  `gmt_modified` varchar(32) DEFAULT '',
  `gmt_creator` varchar(64) DEFAULT '',
  `gmt_modifier` varchar(64) DEFAULT '',
  `buss_no` varchar(64) DEFAULT '' COMMENT '监控项业务编号',
  `item_name` varchar(64) DEFAULT '' COMMENT '监控项名称',
  `item_desc` varchar(1024) DEFAULT '' COMMENT '监控项描述',
  `item_cron` varchar(64) DEFAULT '' COMMENT '监控项频率，cron表达式',
  `item_type` enum('shell','sys') NOT NULL DEFAULT 'sys' COMMENT '类型 shell , sys',
  `orgs` varchar(1024) DEFAULT '' COMMENT '人员组,逗号分隔',
  `exec_content` varchar(4096) DEFAULT '' COMMENT '执行元信息，json格式 ,每种类型都不一样',
  `is_enable` char(1) DEFAULT 'Y' COMMENT '是否启用 Y-启用 N-禁用',
  `is_need_reload` char(1) DEFAULT 'Y' COMMENT '是否需要重载 Y-重载 N-不重载',
  `alarm_max_retry_times` smallint(6) DEFAULT '3' COMMENT '报警最大重试次数',
  `alarm_retry_period` smallint(6) DEFAULT '3' COMMENT '报警重试间隔 单位分钟',
  `tempate_id` bigint(11) DEFAULT NULL COMMENT '报警模板ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='监控项表';

CREATE TABLE `monitor_alarm` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `gmt_created` DATETIME DEFAULT NULL,
  `gmt_modified` varchar(32) DEFAULT '',
  `gmt_creator` varchar(64) DEFAULT '',
  `gmt_modifier` varchar(64) DEFAULT '',
  `buss_no` varchar(1024) DEFAULT '' COMMENT '业务编号,如果是数据流 则为flow_id ，也可能是其他信息 如IP',
  `buss_name` varchar(1024) DEFAULT '' COMMENT '业务名称',
  `buss_desc` varchar(4096) DEFAULT '' COMMENT '业务描述',
  `template_name` varchar(128) DEFAULT '' COMMENT '报警模板名称（从监控项获取，或指定，没有指定则，程序获取默认报警模板填充，以业务名称做title，业务编号 + 业务描述做内容）',
  `alarm_users` varchar(1024) DEFAULT '' COMMENT '邮件或手机号，逗号分隔',
  `send_channel` enum('email','sms') NOT NULL DEFAULT 'email' COMMENT '发送渠道 email-邮件 sms-短信',
  `alarm_max_retry_times` smallint(6) DEFAULT '3' COMMENT '报警最大重试次数',
  `alarm_retry_period` smallint(6) DEFAULT '3' COMMENT '报警重试间隔 单位分钟',
  `send_type` enum('realtime','fixtime') DEFAULT 'realtime' COMMENT '发送类型 realtime  fixtime',
  `fix_datetime` varchar(64) DEFAULT NULL COMMENT '定时yyyy-MM-dd HH:mm:00 ',
  `ext_param` varchar(4096) DEFAULT NULL COMMENT '扩展参数 json ',
  PRIMARY KEY (`id`,`gmt_created`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='报警表 按月分区'
  PARTITION BY RANGE(TO_DAYS (`gmt_created`))
  (
  PARTITION p201506 VALUES LESS THAN (TO_DAYS('2015-07-01')),
  PARTITION p201507 VALUES LESS THAN (TO_DAYS('2015-08-01')),
  PARTITION p201508 VALUES LESS THAN (TO_DAYS('2015-09-01')),
  PARTITION p201509 VALUES LESS THAN (TO_DAYS('2015-10-01')),
  PARTITION p201510 VALUES LESS THAN (TO_DAYS('2015-11-01')),
  PARTITION p201511 VALUES LESS THAN (TO_DAYS('2015-12-01')),
  PARTITION p201512 VALUES LESS THAN (TO_DAYS('2016-01-01')),
  PARTITION p201601 VALUES LESS THAN (TO_DAYS('2016-02-01')),
  PARTITION p201602 VALUES LESS THAN (TO_DAYS('2016-03-01')),
  PARTITION p201603 VALUES LESS THAN (TO_DAYS('2016-04-01')),
  PARTITION p201604 VALUES LESS THAN (TO_DAYS('2016-05-01')),
  PARTITION p201605 VALUES LESS THAN (TO_DAYS('2016-06-01')),
  PARTITION p201606 VALUES LESS THAN (TO_DAYS('2016-07-01')),
  PARTITION p201607 VALUES LESS THAN (TO_DAYS('2016-08-01')),
  PARTITION p201608 VALUES LESS THAN (TO_DAYS('2016-09-01')),
  PARTITION p201609 VALUES LESS THAN (TO_DAYS('2016-10-01')),
  PARTITION p201610 VALUES LESS THAN (TO_DAYS('2016-11-01')),
  PARTITION p201611 VALUES LESS THAN (TO_DAYS('2016-12-01')),
  PARTITION p201612 VALUES LESS THAN (TO_DAYS('2017-01-01')),
  PARTITION p201701 VALUES LESS THAN (TO_DAYS('2017-02-01')),
  PARTITION p201702 VALUES LESS THAN (TO_DAYS('2017-03-01')),
  PARTITION p201703 VALUES LESS THAN (TO_DAYS('2017-04-01')),
  PARTITION p201704 VALUES LESS THAN (TO_DAYS('2017-05-01')),
  PARTITION p201705 VALUES LESS THAN (TO_DAYS('2017-06-01')),
  PARTITION p201706 VALUES LESS THAN (TO_DAYS('2017-07-01')),
  PARTITION p201707 VALUES LESS THAN (TO_DAYS('2017-08-01')),
  PARTITION p201708 VALUES LESS THAN (TO_DAYS('2017-09-01')),
  PARTITION p201709 VALUES LESS THAN (TO_DAYS('2017-10-01')),
  PARTITION p201710 VALUES LESS THAN (TO_DAYS('2017-11-01')),
  PARTITION p201711 VALUES LESS THAN (TO_DAYS('2017-12-01')),
  PARTITION p201801 VALUES LESS THAN (TO_DAYS('2018-01-01'))
  );

CREATE TABLE `monitor_data_records_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_created` varchar(32) DEFAULT '',
  `gmt_modified` varchar(32) DEFAULT '',
  `gmt_creator` varchar(64) DEFAULT '',
  `gmt_modifier` varchar(64) DEFAULT '',
  `data_type_name` varchar(64) DEFAULT '' COMMENT '数据类型名称',
  `data_type_desc` varchar(64) DEFAULT '' COMMENT '数据类型描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='监控数据类型字典表';

CREATE TABLE `monitor_data_records_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_created` varchar(32) DEFAULT '',
  `gmt_modified` varchar(32) DEFAULT '',
  `gmt_creator` varchar(64) DEFAULT '',
  `gmt_modifier` varchar(64) DEFAULT '',
  `buss_no` varchar(128) DEFAULT '' COMMENT '业务编号，用于过滤非法请求，唯一索引',
  `buss_desc` varchar(512) DEFAULT '' COMMENT '业务描述',
  `is_enable` char(1) DEFAULT 'Y' COMMENT '是否启用 Y-启用 N-禁用',
  `data_type_ids` varchar(512) DEFAULT '' COMMENT '数据类型，用于分类',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_buss_no` (`buss_no`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='监控数据配置表';


CREATE TABLE `monitor_data_records` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `gmt_created` DATETIME DEFAULT NULL,
  `gmt_modified` varchar(32) DEFAULT '',
  `gmt_creator` varchar(64) DEFAULT '',
  `gmt_modifier` varchar(64) DEFAULT '',
  `buss_no` varchar(1024) DEFAULT '' COMMENT '业务编号,如果是数据流 则为flow_id ，也可能是其他信息 如IP',
  `send_timestamp` varchar(64) DEFAULT '' COMMENT '发送者时间戳',
  `total_rows` bigint(11) DEFAULT '0' COMMENT '条数',
  `data_type` varchar(64) DEFAULT '' COMMENT ' 数据类型 任意传入，例如collect_normal 、collect_debug、loadDM_nomnal',
  PRIMARY KEY (`id`,`gmt_created`),
  KEY `idx_timestamp_buss` (`buss_no`(255),`send_timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='监控数据表 按日分区'
  PARTITION BY RANGE(TO_DAYS (`gmt_created`))
  (
  PARTITION p20150716 VALUES LESS THAN (TO_DAYS('2015-07-17'))
  );

CREATE TABLE `monitor_data_records_merge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_created` varchar(32) DEFAULT '',
  `gmt_modified` varchar(32) DEFAULT '',
  `gmt_creator` varchar(64) DEFAULT '',
  `gmt_modifier` varchar(64) DEFAULT '',
  `buss_no` varchar(128) DEFAULT '' COMMENT '业务编号，用于过滤非法请求，唯一索引',
  `merge_timestamp` varchar(64) DEFAULT '' COMMENT '合并时刻，5分钟合并1次类似 2015-06-03 00:05:00, 2015-06-03 00:10:00 ',
  `total_rows` bigint(11) DEFAULT '0' COMMENT '条数',
  `data_type` varchar(128) DEFAULT '' COMMENT ' 数据类型 任意传入，例如collect_normal 、collect_debug、loadDM_nomnal',
  PRIMARY KEY (`id`),
  UNIQUE KEY UQ_DATA(`buss_no`,`merge_timestamp`,`data_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='监控合并数据表';

CREATE TABLE `monitor_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_created` varchar(32) DEFAULT '',
  `gmt_modified` varchar(32) DEFAULT '',
  `gmt_creator` varchar(64) DEFAULT '',
  `gmt_modifier` varchar(64) DEFAULT '',
  `buss_no` varchar(64) DEFAULT '' COMMENT '业务编号 ',
  `rule_type` enum('none','analyse','execute','transform') NOT NULL DEFAULT 'none' COMMENT '规则类型 none-无 analyse-分析 execute-执行 transform-转换',
  `rule_action` varchar(512) DEFAULT '' COMMENT '具体某个类型下的动作 如 regex alarmEmail cache es numLimit 等',
  `action_content` varchar(2048) DEFAULT '' COMMENT 'json串,用于描述rule_action',
  `is_enable` char(1) DEFAULT 'Y' COMMENT '是否启用 Y-启用 N-禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='监控规则表';

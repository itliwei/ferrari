CREATE SCHEMA IF NOT EXISTS `ferrari`;

DROP TABLE IF EXISTS `t_data_change_message`;
CREATE TABLE `t_data_change_message` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
`msg_id` bigint(20) NOT NULL COMMENT '消息ID',
`msg_topic` varchar(255) NOT NULL COMMENT '业务实体',
`msg_tag` varchar(255) NOT NULL COMMENT '业务实体属性',
`msg_key` varchar(255) NOT NULL COMMENT '业务实体的业务主键',
`msg_status` int(4) unsigned NOT NULL COMMENT '0未发送 1 发送失败 2 发送成功 3消费失败 4消费成功 ',
`change_type` int(2) unsigned NOT NULL COMMENT '0新增 1修改 2删除',
`change_data` varchar(3000) NOT NULL COMMENT '',
`change_time` bigint(20) unsigned NOT NULL COMMENT '数据变更的时间yyyyMMddHHmmssSSS',
`produce_time` bigint(20) unsigned NOT NULL COMMENT '数据变更消息的发送时间yyyyMMddHHmmssSSS',
`consume_time` bigint(20) unsigned NOT NULL COMMENT '数据变更消息的消费时间yyyyMMddHHmmssSSS',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
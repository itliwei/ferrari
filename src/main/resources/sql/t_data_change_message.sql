/*
Navicat MySQL Data Transfer

Source Server         : 后台系统研发部测试库
Source Server Version : 50709
Source Host           : 10.16.16.13:3306
Source Database       : inventory

Target Server Type    : MYSQL
Target Server Version : 50709
File Encoding         : 65001

Date: 2017-06-12 11:20:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_data_change_message`
-- ----------------------------
DROP TABLE IF EXISTS `t_data_change_message`;
CREATE TABLE `t_data_change_message` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `msg_id` varchar(30) NOT NULL COMMENT '消息ID',
  `msg_system` varchar(255) NOT NULL COMMENT '消息来源系统',
  `msg_module` varchar(255) NOT NULL COMMENT '消息来源模块',
  `msg_function` varchar(255) NOT NULL COMMENT '消息来源功能',
  `msg_status` int(4) unsigned NOT NULL COMMENT '0未发送 1 发送失败 2 发送成功 3消费失败 4消费成功 ',
  `change_entity_name` varchar(255) NOT NULL COMMENT '变更对象名',
  `change_key` varchar(255) NOT NULL COMMENT '数据的主键',
  `change_type` int(2) unsigned NOT NULL COMMENT '0新增 1修改 2删除',
  `change_data` varchar(3000) NOT NULL COMMENT '变更内容',
  `change_time` bigint(20) unsigned NOT NULL COMMENT '数据变更的时间yyyyMMddHHmmssSSS',
  `produce_time` bigint(20) unsigned NOT NULL COMMENT '数据变更消息的发送时间yyyyMMddHHmmssSSS',
  `consume_time` bigint(20) unsigned NOT NULL COMMENT '数据变更消息的消费时间yyyyMMddHHmmssSSS',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '数据变更消息表';

-- ----------------------------
-- Records of t_data_change_message
-- ----------------------------

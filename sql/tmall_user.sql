/*
Navicat MySQL Data Transfer

Source Server         : linux
Source Server Version : 80022
Source Host           : 192.168.123.130:3306
Source Database       : tmall_user

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2021-05-12 16:13:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` char(32) NOT NULL COMMENT '生成32位uuid做主键',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户密码',
  `openid` varchar(100) DEFAULT NULL COMMENT '微信号',
  `nick_name` varchar(100) DEFAULT NULL COMMENT '用户昵称',
  `name` varchar(20) DEFAULT NULL COMMENT '用户名',
  `phone` varchar(11) DEFAULT NULL COMMENT '用户手机号',
  `email` varchar(30) DEFAULT NULL COMMENT '用户邮箱地址',
  `avatar` varchar(255) DEFAULT NULL COMMENT '用户头像url',
  `certificates_type` varchar(10) DEFAULT NULL COMMENT '证件类型',
  `certificates_no` varchar(30) DEFAULT NULL COMMENT '证件编号',
  `certificates_url` varchar(200) DEFAULT NULL COMMENT '证件url地址',
  `auth_status` tinyint NOT NULL DEFAULT '0' COMMENT '认证状态（0：未认证 1：认证中 2：认证成功 -1：认证失败）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0：锁定 1：正常）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_login_record
-- ----------------------------
DROP TABLE IF EXISTS `user_login_record`;
CREATE TABLE `user_login_record` (
  `id` char(32) NOT NULL,
  `user_id` char(32) DEFAULT NULL,
  `ip` varchar(32) DEFAULT NULL COMMENT '登录ip',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;

/*
Navicat MySQL Data Transfer

Source Server         : linux
Source Server Version : 80022
Source Host           : 192.168.123.130:3306
Source Database       : tmall_category

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2021-04-10 12:30:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for category_info
-- ----------------------------
DROP TABLE IF EXISTS `category_info`;
CREATE TABLE `category_info` (
  `id` char(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL COMMENT '分类图片地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for property
-- ----------------------------
DROP TABLE IF EXISTS `property`;
CREATE TABLE `property` (
  `id` char(32) NOT NULL,
  `category_id` char(32) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;

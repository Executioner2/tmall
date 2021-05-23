/*
Navicat MySQL Data Transfer

Source Server         : linux
Source Server Version : 80022
Source Host           : 192.168.123.130:3306
Source Database       : tmall_product

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2021-04-05 16:25:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for product_image
-- ----------------------------
DROP TABLE IF EXISTS `product_image`;
CREATE TABLE `product_image` (
  `id` char(32) NOT NULL,
  `product_id` char(32) DEFAULT NULL,
  `type` tinyint DEFAULT NULL COMMENT '图片类型(0: 缩略图  1:详情图)',
  `url` varchar(255) DEFAULT NULL COMMENT '图片url地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for product_info
-- ----------------------------
DROP TABLE IF EXISTS `product_info`;
CREATE TABLE `product_info` (
  `id` char(32) NOT NULL,
  `category_id` char(32) DEFAULT NULL COMMENT '分类id',
  `name` varchar(255) DEFAULT NULL,
  `sub_title` varchar(255) DEFAULT NULL COMMENT '小标题',
  `orignal_price` decimal(10,2) DEFAULT NULL COMMENT '产品原价',
  `promote_price` decimal(10,2) DEFAULT NULL COMMENT '产品实际价格',
  `stock` bigint DEFAULT NULL COMMENT '库存数量',
  `sales_volume` bigint DEFAULT NULL COMMENT '总销量',
  `monthly_sales` bigint DEFAULT NULL COMMENT '月销售量',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for property_value
-- ----------------------------
DROP TABLE IF EXISTS `property_value`;
CREATE TABLE `property_value` (
  `id` char(32) NOT NULL,
  `product_id` char(32) DEFAULT NULL,
  `property_id` char(32) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL COMMENT '产品属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` char(32) NOT NULL,
  `user_id` char(32) DEFAULT NULL,
  `product_id` char(32) DEFAULT NULL,
  `content` varchar(4000) DEFAULT NULL COMMENT '评价内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;

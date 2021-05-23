/*
Navicat MySQL Data Transfer

Source Server         : linux
Source Server Version : 80022
Source Host           : 192.168.123.130:3306
Source Database       : tmall_order

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2021-05-22 17:31:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` char(32) NOT NULL,
  `user_id` char(32) DEFAULT NULL COMMENT '用户id',
  `out_trade_no` varchar(30) DEFAULT NULL COMMENT '对外订单号（支付成功上面的商家订单号）',
  `address` varchar(255) DEFAULT NULL COMMENT '收货地址',
  `post` char(6) DEFAULT NULL COMMENT '邮政编码',
  `receiver` varchar(255) DEFAULT NULL COMMENT '收货人',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `user_message` varchar(255) DEFAULT NULL COMMENT '用户留言',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `order_status` tinyint DEFAULT NULL COMMENT '订单状态(0:待付款  1:待发货  2:待收货  3:待评价  4:交易完成)',
  `create_date` datetime DEFAULT NULL COMMENT '订单创建日期',
  `pay_date` datetime DEFAULT NULL COMMENT '支付日期',
  `delivery_date` datetime DEFAULT NULL COMMENT '发货日期',
  `confirm_date` datetime DEFAULT NULL COMMENT '收货日期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据库记录创建时间戳，上面那个是订单创建日期',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` char(32) NOT NULL,
  `order_id` char(32) DEFAULT NULL,
  `product_id` char(32) DEFAULT NULL,
  `user_id` char(32) DEFAULT NULL,
  `number` int DEFAULT NULL COMMENT '商品数量',
  `is_review` tinyint NOT NULL DEFAULT '0' COMMENT '是否评价(0:未评价 1:已评价)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for payment_info
-- ----------------------------
DROP TABLE IF EXISTS `payment_info`;
CREATE TABLE `payment_info` (
  `id` char(32) NOT NULL,
  `out_trade_no` varchar(30) DEFAULT NULL,
  `order_id` char(32) DEFAULT NULL,
  `payment_type` tinyint DEFAULT NULL COMMENT '交易类型（0：微信  1：支付宝）',
  `trade_no` varchar(50) DEFAULT NULL COMMENT '支付平台的订单号',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `subject` varchar(200) DEFAULT NULL COMMENT '交易描述',
  `payment_status` tinyint DEFAULT NULL COMMENT '支付状态（1: 支付中 2: 已支付）',
  `callback_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '回调时间',
  `callback_content` varchar(100) DEFAULT NULL COMMENT '回调信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for refund_info
-- ----------------------------
DROP TABLE IF EXISTS `refund_info`;
CREATE TABLE `refund_info` (
  `id` char(32) NOT NULL,
  `out_trade_no` varchar(30) DEFAULT NULL,
  `order_id` char(32) DEFAULT NULL,
  `payment_type` tinyint DEFAULT NULL COMMENT '支付类型',
  `trade_no` varchar(50) DEFAULT NULL COMMENT '支付平台订单号',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '总金额',
  `subject` varchar(200) DEFAULT NULL COMMENT '交易内容',
  `refund_status` tinyint DEFAULT NULL COMMENT '退款状态（1: 退款中 2: 已退款）',
  `callback_content` varchar(100) DEFAULT NULL COMMENT '回调信息',
  `callback_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '回调日期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;

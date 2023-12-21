/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : android

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2023-12-21 12:51:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收藏商品ID',
  `userId` int(11) NOT NULL COMMENT '用户ID',
  `productId` int(11) NOT NULL COMMENT '商品ID',
  `priceThreshold` double DEFAULT NULL COMMENT '价格下限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收藏商品表';

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `productId` varchar(20) NOT NULL,
  `content` varchar(128) NOT NULL,
  `issueTime` datetime NOT NULL,
  `deleteState` int(1) NOT NULL,
  `isOwner` int(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `userId` int(11) NOT NULL COMMENT '用户ID',
  `productId` int(11) NOT NULL COMMENT '商品ID',
  `time` datetime DEFAULT NULL COMMENT '消息时间',
  `remark` tinytext COMMENT '备注',
  `isRead` tinyint(1) DEFAULT NULL COMMENT '是否已读',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息表';

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `productId` int(11) NOT NULL,
  `orderTime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `productName` varchar(20) NOT NULL,
  `campus` varchar(10) NOT NULL,
  `category` varchar(10) NOT NULL,
  `price` double NOT NULL,
  `description` varchar(128) NOT NULL,
  `issueTime` datetime NOT NULL,
  `soldTime` datetime DEFAULT NULL,
  `imagePath` varchar(50) NOT NULL,
  `soldState` int(1) NOT NULL,
  `deleteState` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `studentId` varchar(20) NOT NULL,
  `password` varchar(100) NOT NULL,
  `salt` varchar(40) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `studentId` (`studentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for visitrecorder
-- ----------------------------
DROP TABLE IF EXISTS `visitrecorder`;
CREATE TABLE `visitrecorder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `productId` int(11) NOT NULL,
  `visitTime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

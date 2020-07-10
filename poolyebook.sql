/*
Navicat MySQL Data Transfer

Source Server         : 本机MySQL
Source Server Version : 50619
Source Host           : localhost:3306
Source Database       : poolyebook

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2020-07-10 10:55:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `uuid` varchar(36) NOT NULL,
  `name` varchar(100) NOT NULL,
  `state` int(11) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `UK_wugryet8mf6oi28n00x2eoc4` (`name`),
  KEY `IDX37snxbef108jsqd1kv1hci04a` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for catelog
-- ----------------------------
DROP TABLE IF EXISTS `catelog`;
CREATE TABLE `catelog` (
  `uuid` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `catelog_order` int(11) NOT NULL,
  `state` int(11) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `book_id` varchar(36) NOT NULL,
  PRIMARY KEY (`uuid`),
  KEY `IDXsk1n601xnc3j7ivab9ijvibq1` (`state`),
  KEY `IDX92osatvp9cwm7xcfk95iv7a3u` (`catelog_order`),
  KEY `FK76exwthfisns0wjw3bjjdt15s` (`book_id`),
  CONSTRAINT `FK76exwthfisns0wjw3bjjdt15s` FOREIGN KEY (`book_id`) REFERENCES `book` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

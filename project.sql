/*
 Navicat Premium Data Transfer

 Source Server         : demo1
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : project

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 03/04/2023 17:15:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for equipment
-- ----------------------------
DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` int NULL DEFAULT NULL COMMENT '1:产线',
  `status` int NULL DEFAULT NULL COMMENT '1:在线,0:离线',
  `introduce` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产线的介绍',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备或者产线的名字',
  `ftpInfostatus` int NULL DEFAULT NULL COMMENT '是否已经读取 0:未读取,1:以读取',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of equipment
-- ----------------------------
INSERT INTO `equipment` VALUES (1, 1, 1, '1', '设备名字1', 1);
INSERT INTO `equipment` VALUES (2, 1, 0, '2', '设备名字2', 1);
INSERT INTO `equipment` VALUES (3, 1, -1, '设备介绍3', '设备名字3', 1);
INSERT INTO `equipment` VALUES (4, 0, 1, '设备介绍4', '设备名字4', 1);
INSERT INTO `equipment` VALUES (5, 1, 0, '设备介绍5', '设备名字5', 0);
INSERT INTO `equipment` VALUES (6, NULL, 1, '设备介绍6', '设备名字6', 0);
INSERT INTO `equipment` VALUES (7, NULL, 1, '设备介绍7', '设备名字7', 0);

-- ----------------------------
-- Table structure for fileinfo
-- ----------------------------
DROP TABLE IF EXISTS `fileinfo`;
CREATE TABLE `fileinfo`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `fetchtime` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '1:良品,2次品',
  `equipmentid` int NULL DEFAULT NULL COMMENT '对应的设备id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 285 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fileinfo
-- ----------------------------

-- ----------------------------
-- Table structure for productline
-- ----------------------------
DROP TABLE IF EXISTS `productline`;
CREATE TABLE `productline`  (
  `id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产线的名字',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产线\r\n产线',
  `status` int NULL DEFAULT NULL COMMENT '1.在线,0离线,-1故障',
  `introduce` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产线介绍',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of productline
-- ----------------------------
INSERT INTO `productline` VALUES (1, '产线名字', '1', 1, '产线介绍1');
INSERT INTO `productline` VALUES (2, '产线名字2', '11', 2, '产线介绍2');
INSERT INTO `productline` VALUES (3, '产线介绍3', '1', 0, '产线介绍3');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `passWord` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('123', '123');
INSERT INTO `user` VALUES ('123', '222');
INSERT INTO `user` VALUES ('user', 'name');
INSERT INTO `user` VALUES ('admin', 'admin');

SET FOREIGN_KEY_CHECKS = 1;

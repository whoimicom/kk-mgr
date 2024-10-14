/*
 Navicat Premium Data Transfer

 Source Server         : KINKIM
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Schema         : kinkim

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 23/05/2021 20:16:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for authorities
-- ----------------------------
DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `authority` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  UNIQUE INDEX `ix_auth_username`(`username`, `authority`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authorities
-- ----------------------------
INSERT INTO `authorities` VALUES ('admin', '/hello');

-- ----------------------------
-- Table structure for group_authorities
-- ----------------------------
DROP TABLE IF EXISTS `group_authorities`;
CREATE TABLE `group_authorities`  (
  `group_id` bigint(0) NOT NULL,
  `authority` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_members
-- ----------------------------
DROP TABLE IF EXISTS `group_members`;
CREATE TABLE `group_members`  (
  `id` bigint(0) NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `group_id` bigint(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups`  (
  `id` bigint(0) NOT NULL,
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kk_user_info
-- ----------------------------
DROP TABLE IF EXISTS `kk_user_info`;
CREATE TABLE `kk_user_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `created_by` varchar(55) DEFAULT NULL,
  `created_datetime` datetime DEFAULT NULL,
  `last_modified_by` varchar(55) DEFAULT NULL,
  `last_modified_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=633 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- ----------------------------
-- Records of kk_user_info
-- ----------------------------
INSERT INTO `kinkim`.`kk_user_info`(`id`, `username`, `password`, `enabled`, `avatar`, `introduction`, `email`, `mobile`, `gender`, `created_by`, `created_date_time`, `last_modified_by`, `last_modified_datetime`) VALUES (1, 'kinkim', '$2a$10$fm3xLyxsOXYcCobbb5FKp.3mdY3PWgIWYKy8jZWTL.z2SpU6RtDMK', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `kinkim`.`kk_user_info`(`id`, `username`, `password`, `enabled`, `avatar`, `introduction`, `email`, `mobile`, `gender`, `created_by`, `created_date_time`, `last_modified_by`, `last_modified_datetime`) VALUES (2, 'admin', '$2a$10$RBu1rjIPbuVh8VeAMcOuvO8FmxW6KympyTsz2U0zCz95M773nLVbi', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins`  (
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `series` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `last_used` timestamp(0) NOT NULL,
  PRIMARY KEY (`series`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

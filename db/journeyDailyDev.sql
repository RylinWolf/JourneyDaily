-- MySQL dump 10.13  Distrib 9.0.1, for macos14.4 (arm64)
--
-- Host: 127.0.0.1    Database: journeyDailyDev
-- ------------------------------------------------------
-- Server version	9.0.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

use journeyDaily;



--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin`
(
    `admin_id` bigint       NOT NULL AUTO_INCREMENT COMMENT '管理员 ID',
    `user_id`  bigint       NOT NULL COMMENT '用户 ID',
    `purview`  int          NOT NULL DEFAULT '0' COMMENT '权限 0-全部，1-仅用户，2-仅管理员',
    `password` varchar(255) NOT NULL COMMENT '管理员密码',
    PRIMARY KEY (`admin_id`),
    UNIQUE KEY `admin_id_user_id_index` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10030
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category`
(
    `category_id` bigint       NOT NULL,
    `user_id`     bigint       NOT NULL,
    `title`       varchar(255) NOT NULL COMMENT '分类标题',
    `info`        text COMMENT '分类简介',
    `parent`      bigint DEFAULT NULL COMMENT '分类所属上级',
    PRIMARY KEY (`category_id`, `user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `draft_journeys`
--

DROP TABLE IF EXISTS `draft_journeys`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `draft_journeys`
(
    `user_id`    bigint NOT NULL COMMENT '用户 ID',
    `journey_id` bigint NOT NULL COMMENT '日记 ID',
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='暂存文章';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `journey`
--

DROP TABLE IF EXISTS `journey`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `journey`
(
    `journey_id`   bigint       NOT NULL AUTO_INCREMENT,
    `title`        varchar(255) NOT NULL COMMENT '日记标题',
    `content`      text         NOT NULL COMMENT '日记内容',
    `summary`      text COMMENT '日记总结',
    `author_id`    bigint                DEFAULT NULL,
    `post_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `edit_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `visibility`   tinyint      NOT NULL DEFAULT '0',
    `is_delete`    int          NOT NULL DEFAULT '0',
    `partition_id` bigint                DEFAULT NULL COMMENT '分区',
    `length`       int                   DEFAULT '0' COMMENT '字数统计',
    PRIMARY KEY (`journey_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10050
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `journey_label`
--

DROP TABLE IF EXISTS `journey_label`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `journey_label`
(
    `journey_id` bigint NOT NULL COMMENT '日记 ID',
    `label_id`   bigint NOT NULL COMMENT '标签 ID',
    PRIMARY KEY (`journey_id`, `label_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='日记-标签表，存储日记与标签的关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `journey_partition`
--

DROP TABLE IF EXISTS `journey_partition`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `journey_partition`
(
    `partition_id` bigint       NOT NULL AUTO_INCREMENT COMMENT '分区 ID',
    `user_id`      bigint       NOT NULL COMMENT '分区所属用户 ID',
    `title`        varchar(255) NOT NULL COMMENT '分区标题',
    `info`         text COMMENT '分区简介',
    `parent`       bigint DEFAULT NULL COMMENT '分区所属上级',
    PRIMARY KEY (`partition_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10008
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='日记分区表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS `label`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `label`
(
    `label_id` bigint       NOT NULL AUTO_INCREMENT COMMENT '标签 ID',
    `name`     varchar(100) NOT NULL COMMENT '标签名',
    PRIMARY KEY (`label_id`),
    UNIQUE KEY `label_id_name_idx` (`label_id`, `name`),
    UNIQUE KEY `label_pk` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user`
(
    `user_id`   bigint       NOT NULL AUTO_INCREMENT,
    `user_name` varchar(255) NOT NULL COMMENT '用户名',
    `email`     varchar(255)          DEFAULT NULL COMMENT '邮箱地址',
    `pwd_hash`  varchar(255) NOT NULL COMMENT '密码哈希',
    `tagline`   varchar(255)          DEFAULT NULL COMMENT '个性签名',
    `is_delete` tinyint      NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `is_admin`  tinyint      NOT NULL DEFAULT '0' COMMENT '是否管理员，0-否，1-是',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `email` (`email`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100002
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_label`
--

DROP TABLE IF EXISTS `user_label`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_label`
(
    `user_id`  bigint NOT NULL COMMENT '用户 ID',
    `label_id` bigint NOT NULL COMMENT '标签 ID',
    PRIMARY KEY (`user_id`, `label_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户-标签表，存储用户创建的标签';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2025-05-22 10:49:53

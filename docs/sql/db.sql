DROP DATABASE IF EXISTS tutor_db;
CREATE DATABASE tutor_db;
USE tutor_db;

-- 用户表
CREATE TABLE `user` (
                        `id` bigint PRIMARY KEY AUTO_INCREMENT,
                        `phone` varchar(11) NOT NULL UNIQUE,
                        `password` varchar(100) NOT NULL,
                        `role` varchar(10) NOT NULL,
                        `nickname` varchar(50)
);

-- 需求表
CREATE TABLE `demand` (
                          `id` bigint PRIMARY KEY AUTO_INCREMENT,
                          `parent_id` bigint NOT NULL,
                          `subject` varchar(50) NOT NULL,
                          `grade` varchar(20),
                          `price` int,
                          `description` text,
                          `status` int DEFAULT 0,   -- 0:待接单 1:已接单
                          `create_time` datetime DEFAULT CURRENT_TIMESTAMP
);

-- 订单表
CREATE TABLE `tutor_order` (
                               `id` bigint PRIMARY KEY AUTO_INCREMENT,
                               `demand_id` bigint NOT NULL,
                               `teacher_id` bigint NOT NULL,
                               `parent_id` bigint NOT NULL,
                               `status` int DEFAULT 0,   -- 0:待确认 1:已完成
                               `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                               `complete_time` datetime
);
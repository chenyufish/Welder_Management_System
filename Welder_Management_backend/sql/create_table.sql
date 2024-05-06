# 数据库初始化
-- 创建库
create database if not exists fishmanwm;

-- 切换库
use fishmanwm;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
                         `password` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
                         `userAccount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
                         `avatarUrl` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
                         `gender` tinyint(4) NULL DEFAULT NULL COMMENT '性别 0-女 1-男',
                         `profile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                         `phone` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
                         `email` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                         `status` int(11) NULL DEFAULT 0 COMMENT '用户状态，0为正常',
                         `role` int(11) NOT NULL DEFAULT 0 COMMENT '用户角色 0-普通用户,1-管理员',
                         `friendIds` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                         `tags` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签列表',
                         `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE `WeldingMachines` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 machineName varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '焊机名字',
                                 serialNumber VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '焊机序列号',
                                 usageHours int(11) COMMENT '已使用时间（小时）',
                                 imagePath VARCHAR(255) COMMENT '焊机图片路径',
                                 notes text COMMENT '备注，额外信息',
                                 machineStatus int(11) NOT NULL DEFAULT 0 COMMENT '焊机状态（0-空闲，1-使用中，2-维修中）',
                                `tags` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签列表',
                                `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE WeldingUsage (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                              machineID bigint(20) NOT NULL  COMMENT '焊机ID',
                              employeeID bigint(20) NOT NULL COMMENT '员工ID',
                              `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借用时间',
                              `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '归还时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;


CREATE TABLE WeldingMachineFaults (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                      machineID bigint(20) NOT NULL  COMMENT '焊机ID',
                                      employeeID bigint(20) NOT NULL COMMENT '员工ID',
                                      description text NOT NULL COMMENT '故障描述',
                                      status int(11) NOT NULL DEFAULT 0 COMMENT '处理状态（0-待处理，1-已处理）',
                                      imagePath VARCHAR(255) COMMENT '故障图片路径',
                                      `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;
-- Table structure for blog
-- ----------------------------
-- 管理通知表
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`  (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                         `userId` bigint(20) NOT NULL COMMENT '用户id',
                         `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
                         `images` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片，最多9张，多张以\",\"隔开',
                         `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章',
                         `likedNum` int(8) UNSIGNED NULL DEFAULT 0 COMMENT '点赞数量',
                         `commentsNum` int(8) UNSIGNED NULL DEFAULT 0 COMMENT '评论数量',
                         `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for blog_comments
-- ----------------------------
-- 通知品论表
DROP TABLE IF EXISTS `blog_comments`;
CREATE TABLE `blog_comments`  (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `userId` bigint(20) NOT NULL COMMENT '用户id',
                                  `blogId` bigint(20) NOT NULL COMMENT '博文id',
                                  `parentId` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '关联的1级评论id，如果是一级评论，则值为0',
                                  `answerId` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '回复的评论id',
                                  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回复的内容',
                                  `likedNum` int(8) UNSIGNED NULL DEFAULT 0 COMMENT '点赞数',
                                  `status` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '状态，0：正常，1：被举报，2：禁止查看',
                                  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for blog_like
-- ----------------------------
-- 通知点赞表
DROP TABLE IF EXISTS `blog_like`;
CREATE TABLE `blog_like`  (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `blogId` bigint(20) NOT NULL COMMENT '博文id',
                              `userId` bigint(20) NOT NULL COMMENT '用户id',
                              `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `updateTime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `isDelete` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;


-- ----------------------------
-- Table structure for comment_like
-- ----------------------------
-- 评论点赞表
DROP TABLE IF EXISTS `comment_like`;
CREATE TABLE `comment_like`  (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `commentId` bigint(20) NOT NULL COMMENT '评论id',
                                 `userId` bigint(20) NOT NULL COMMENT '用户id',
                                 `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `isDelete` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = COMPACT;


-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                            `type` tinyint(4) NULL DEFAULT NULL COMMENT '类型-1 点赞',
                            `fromId` bigint(20) NULL DEFAULT NULL COMMENT '消息发送的用户id',
                            `toId` bigint(20) NULL DEFAULT NULL COMMENT '消息接收的用户id',
                            `data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容',
                            `isRead` tinyint(4) NULL DEFAULT 0 COMMENT '已读-0 未读 ,1 已读',
                            `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `isDelete` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = COMPACT;


-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat`  (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '聊天记录id',
                         `fromId` bigint(20) NOT NULL COMMENT '发送消息id',
                         `toId` bigint(20) NULL DEFAULT NULL COMMENT '接收消息id',
                         `text` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                         `chatType` tinyint(4) NOT NULL COMMENT '聊天类型 1-私聊 2-群聊',
                         `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                         `teamId` bigint(20) NULL DEFAULT NULL,
                         `isDelete` tinyint(4) NULL DEFAULT 0,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天消息表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends`  (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '好友申请id',
                            `fromId` bigint(20) NOT NULL COMMENT '发送申请的用户id',
                            `receiveId` bigint(20) NULL DEFAULT NULL COMMENT '接收申请的用户id ',
                            `isRead` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否已读(0-未读 1-已读)',
                            `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '申请状态 默认0 （0-未通过 1-已同意 2-已过期 3-已撤销）',
                            `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                            `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
                            `remark` varchar(214) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '好友申请备注信息',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '好友申请管理表' ROW_FORMAT = COMPACT;
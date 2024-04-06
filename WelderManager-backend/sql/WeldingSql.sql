DROP TABLE IF EXISTS `Weldinguser`;
CREATE TABLE `Weldinguser`  (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
                                `password` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
                                `user_account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号',
                                `avatar_url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
                                `gender` tinyint(4) NULL DEFAULT NULL COMMENT '性别 0-女 1-男 2-保密',
                                `profile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                `phone` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
                                `email` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                                `status` int(11) NULL DEFAULT 0 COMMENT '员工状态（0-正常在岗，1-请假离岗，2-未请假离岗，3-离职）',
                                `role` int(11) NOT NULL DEFAULT 0 COMMENT '用户角色 0-普通用户,1-管理员',
                                `friend_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                `tags` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签列表',
                                `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                `is_delete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXISTS `WeldingMachines`;
CREATE TABLE WeldingMachines (
                                 machine_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'machine_id',
                                 brand VARCHAR(100) COMMENT '焊机品牌',
                                 model VARCHAR(100) COMMENT '焊机型号',
                                 serial_number VARCHAR(100) COMMENT '焊机序列号',
                                 purchase_date DATE COMMENT '购买日期',
                                 location VARCHAR(255) COMMENT '焊机所在位置',
                                 voltage DECIMAL(5,2) COMMENT '电压，单位：伏特',
                                 current DECIMAL(5,2) COMMENT '电流，单位：安培',
                                 electrode_diameter DECIMAL(5,2) COMMENT '焊条直径，单位：毫米',
                                 last_maintenance_date DATE COMMENT '上次维修时间',
                                 usage_hours INT COMMENT '已使用时间（小时）',
                                 welding_method VARCHAR(50) COMMENT '焊接方式',
                                 image_path VARCHAR(255) COMMENT '焊机图片路径',
                                 notes TEXT COMMENT '备注，额外信息',
                                 machine_status int(11) NULL DEFAULT 0 COMMENT '焊机状态（0-空闲，1-使用中，2-维修中）',
                                 PRIMARY KEY (`machine_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `WeldingUsage`;
CREATE TABLE WeldingUsage (
                              borrow_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'borrow_id',
                              machine_id INT COMMENT '焊机ID，关联焊机表中的MachineID',
                              employee_id INT COMMENT '员工ID，关联员工表中的id',
                              borrow_time DATETIME COMMENT '借用时间',
                              return_time DATETIME COMMENT '归还时间',
                              PRIMARY KEY (`borrow_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `WeldingMachineFaults`;
CREATE TABLE WeldingMachineFaults (
                                      fault_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'fault_id',
                                      machine_id INT COMMENT '焊机ID，关联焊机表中的MachineID',
                                      employee_id INT COMMENT '员工ID，关联员工表中的id',
                                      report_time DATETIME COMMENT '报告时间',
                                      description TEXT COMMENT '故障描述',
                                      resolution TEXT COMMENT '处理方法',
                                      status int(11) NULL DEFAULT 0 COMMENT '处理状态（0-待处理，1-已处理）',
                                      image_path VARCHAR(255) COMMENT '故障图片路径',
                                      PRIMARY KEY (`fault_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

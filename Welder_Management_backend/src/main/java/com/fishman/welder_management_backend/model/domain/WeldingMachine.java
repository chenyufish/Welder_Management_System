package com.fishman.welder_management_backend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName weldingmachines
 */
@TableName(value ="weldingmachines")
@Data
public class WeldingMachine implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 焊机名字
     */
    private String machineName;

    /**
     * 焊机序列号
     */
    private String serialNumber;

    /**
     * 已使用时间（小时）
     */
    private Integer usageHours;

    /**
     * 焊机图片路径
     */
    private String imagePath;

    /**
     * 备注，额外信息
     */
    private String notes;

    /**
     * 焊机状态（0-空闲，1-使用中，2-维修中）
     */
    private Integer machineStatus;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.fishman.welder_management_backend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName weldingmachinefaults
 */
@TableName(value ="weldingmachinefaults")
@Data
public class WeldingMachineFaults implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 焊机ID
     */
    private Long machineID;

    /**
     * 员工ID
     */
    private Long employeeID;

    /**
     * 故障描述
     */
    private String description;

    /**
     * 处理状态（0-待处理，1-已处理）
     */
    private Integer status;

    /**
     * 故障图片路径
     */
    private String imagePath;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    private String title;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.fishman.welder_management_backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName weldingusage
 */
@TableName(value ="weldingusage")
@Data
public class WeldingUsage implements Serializable {
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
     * 使用人数（小时）
     */
    private Integer usageNum;

    /**
     * 借用时间
     */
    private Date createTime;

    /**
     * 归还时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
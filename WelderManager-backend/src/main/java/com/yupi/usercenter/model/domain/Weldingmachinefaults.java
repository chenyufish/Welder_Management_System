package com.yupi.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName weldingmachinefaults
 */
@TableName(value ="weldingmachinefaults")
@Data
public class Weldingmachinefaults implements Serializable {
    /**
     * fault_id
     */
    @TableId(type = IdType.AUTO)
    private Long fault_id;

    /**
     * 焊机ID，关联焊机表中的MachineID
     */
    private Integer machine_id;

    /**
     * 员工ID，关联员工表中的id
     */
    private Integer employee_id;

    /**
     * 报告时间
     */
    private Date report_time;

    /**
     * 故障描述
     */
    private String description;

    /**
     * 处理方法
     */
    private String resolution;

    /**
     * 处理状态（0-待处理，1-已处理）
     */
    private Integer status;

    /**
     * 故障图片路径
     */
    private String image_path;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
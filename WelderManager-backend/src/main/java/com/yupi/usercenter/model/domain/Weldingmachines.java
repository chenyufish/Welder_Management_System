package com.yupi.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName weldingmachines
 */
@TableName(value ="weldingmachines")
@Data
public class Weldingmachines implements Serializable {
    /**
     * machine_id
     */
    @TableId(type = IdType.AUTO)
    private Long machine_id;

    /**
     * 焊机品牌
     */
    private String brand;

    /**
     * 焊机型号
     */
    private String model;

    /**
     * 焊机序列号
     */
    private String serial_number;

    /**
     * 购买日期
     */
    private Date purchase_date;

    /**
     * 焊机所在位置
     */
    private String location;

    /**
     * 电压，单位：伏特
     */
    private BigDecimal voltage;

    /**
     * 电流，单位：安培
     */
    private BigDecimal current;

    /**
     * 焊条直径，单位：毫米
     */
    private BigDecimal electrode_diameter;

    /**
     * 上次维修时间
     */
    private Date last_maintenance_date;

    /**
     * 已使用时间（小时）
     */
    private Integer usage_hours;

    /**
     * 焊接方式
     */
    private String welding_method;

    /**
     * 焊机图片路径
     */
    private String image_path;

    /**
     * 备注，额外信息
     */
    private String notes;

    /**
     * 焊机状态（0-空闲，1-使用中，2-维修中）
     */
    private Integer machine_status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
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
 * @TableName weldingusage
 */
@TableName(value ="weldingusage")
@Data
public class Weldingusage implements Serializable {
    /**
     * borrow_id
     */
    @TableId(type = IdType.AUTO)
    private Long borrow_id;

    /**
     * 焊机ID，关联焊机表中的MachineID
     */
    private Integer machine_id;

    /**
     * 员工ID，关联员工表中的id
     */
    private Integer employee_id;

    /**
     * 借用时间
     */
    private Date borrow_time;

    /**
     * 归还时间
     */
    private Date return_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
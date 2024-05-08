package com.fishman.welder_management_backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 添加设备请求
 *
 * @author fishman
 *
 */
@Data
@ApiModel(value = "添加设备请求")
public class MachineAddRequest implements Serializable {
    /**
     * 串行版本uid
     */
    private static final long serialVersionUID = 8901928610877984172L;
    /**
     * 设备名
     */

    @ApiModelProperty(value = "设备名称")
    private String machineName;
    /**
     * 设备序列号
     */
    @ApiModelProperty(value = "设备序列号")
    private String serialNumber;

    @ApiModelProperty(value = "设备描述")
    /**
     * 介绍
     */
    private String notes;
    /**
     * 0 - 空闲，1 - 使用，2 - 维修
     */
    @ApiModelProperty(value = "状态0 - 空闲，1 - 使用，2 - 维修")
    private Integer machineStatus;

}

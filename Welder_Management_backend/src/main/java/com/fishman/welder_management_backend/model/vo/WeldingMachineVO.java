package com.fishman.welder_management_backend.model.vo;


import com.fishman.welder_management_backend.model.domain.Blog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 设备vo
 *
 * @author fishman
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "设备返回")
public class WeldingMachineVO extends Blog implements Serializable {
    /**
     * 串行版本uid
     */
    private static final long serialVersionUID = -1461567317259590205L;

    /**
     * 封面图片
     */
    @ApiModelProperty(value = "封面图片")
    private String imagePath;
    /**
     * 焊机状态（0-空闲，1-使用中，2-维修中）
     */
    private Integer machineStatus;
    /**
     * 已使用时间（小时）
     */
    private Integer usageHours;

}

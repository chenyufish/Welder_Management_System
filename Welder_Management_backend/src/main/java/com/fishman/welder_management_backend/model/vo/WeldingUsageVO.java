package com.fishman.welder_management_backend.model.vo;


import com.fishman.welder_management_backend.model.domain.Blog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.aspectj.apache.bcel.generic.LineNumberGen;

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
public class WeldingUsageVO extends Blog implements Serializable {
    /**
     * 串行版本uid
     */
    private static final long serialVersionUID = -5719387361771906622L;

   private Long machineId;
   private Long employeeId;
    /**
     * 0 - 空闲，1 - 使用，2 - 故障
     */
    @ApiModelProperty(value = "状态")
    private Integer machineStatus;

}

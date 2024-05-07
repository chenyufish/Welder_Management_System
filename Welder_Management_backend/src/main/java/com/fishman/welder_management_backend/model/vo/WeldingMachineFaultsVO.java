package com.fishman.welder_management_backend.model.vo;


import com.fishman.welder_management_backend.model.domain.Blog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备vo
 *
 * @author fishman
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "设备返回")
public class WeldingMachineFaultsVO extends Blog implements Serializable {
    /**
     * 串行版本uid
     */
    private static final long serialVersionUID = -1891573101324912504L;

    /**
     * 焊机ID
     */
    private Long machineID;

    /**
     * 员工ID
     */
    private Long employeeID;

    private String title;
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

}

package com.fishman.welder_management_backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private MultipartFile[] imagePath;
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

}

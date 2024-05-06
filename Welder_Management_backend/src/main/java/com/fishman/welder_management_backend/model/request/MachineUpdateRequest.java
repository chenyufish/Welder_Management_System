package com.fishman.welder_management_backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 博客更新请求
 *
 */
@Data
@ApiModel(value = "更新博文请求")
public class MachineUpdateRequest implements Serializable {
    /**
     * 串行版本uid
     */
    private static final long serialVersionUID = -3573976626274326698L;
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * img str
     */
    @ApiModelProperty(value = "已上传的图片")
    private String imgStr;
    /**
     * 图片
     */
    @ApiModelProperty(value = "未上传的图片")
    private MultipartFile[] imagePath;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String MachineName;
    /**
     * 设备状态
     */
    @ApiModelProperty(value = "设备状态")
    private String MachineStatus;
    /**
     * 设备备注
     */
    @ApiModelProperty(value = "设备备注")
    private String note;
    /**
     * 设备标签
     */
    @ApiModelProperty(value = "设备标签")
    private String tags;
}

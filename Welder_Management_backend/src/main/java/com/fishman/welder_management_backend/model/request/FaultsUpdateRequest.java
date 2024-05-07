package com.fishman.welder_management_backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 博客添加请求
 *

 */
@Data
@ApiModel(value = "故障申请请求")
public class FaultsUpdateRequest implements Serializable {
    /**
     * 串行版本uid
     */
    private static final long serialVersionUID = 5728526313765902301L;
    /**
     * 图片
     */
    @ApiModelProperty(value = "已上传的图片")
    private String imgStr;
    /**
     * 图片
     */
    @ApiModelProperty(value = "未上传的图片")
    private MultipartFile[] images;
    /**
     * 焊机ID
     */
    private Long machineID;


    /**
     * 故障描述
     */
    private String description;

    private String title;
}

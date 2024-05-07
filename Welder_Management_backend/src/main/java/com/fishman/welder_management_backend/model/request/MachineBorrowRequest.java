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
@ApiModel(value = "借用设备请求")
public class MachineBorrowRequest implements Serializable {
    /**
     * 串行版本uid
     */
    private static final long serialVersionUID = -5504967867469719552L;
    /**
     * 焊机ID
     */
    private Long machineID;

    /**
     * 员工ID
     */
    private Long employeeID;

}

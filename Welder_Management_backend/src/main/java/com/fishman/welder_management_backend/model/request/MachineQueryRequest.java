package com.fishman.welder_management_backend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 团队查询请求
 *
 */
@Data
@ApiModel(value = "设备搜索请求")
public class MachineQueryRequest implements Serializable {
    private static final long serialVersionUID = -8275513294306729202L;
    /**
     * id
     */
    @ApiModelProperty(value = "设备id")
    private Long id;

    /**
     * id 列表
     */
    @ApiModelProperty(value = "id列表")
    private List<Long> idList;

    /**
     * 搜索关键词（同时对队伍名称和描述搜索）
     */
    @ApiModelProperty(value = "搜索关键词")
    private String searchText;

    /**
     * 队伍名称
     */
    @ApiModelProperty(value = "设备名称")
    private String machineName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;


    /**
     * 用户id
     */
    @ApiModelProperty(value = "创建者id")
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    @ApiModelProperty(value = "状态0 - 空闲，1 - 使用中，2 - 故障")
    private Integer machineStatus;

}

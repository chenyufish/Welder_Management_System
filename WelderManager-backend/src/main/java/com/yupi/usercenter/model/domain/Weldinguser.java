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
 * @TableName weldinguser
 */
@TableName(value ="weldinguser")
@Data
public class Weldinguser implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 工号
     */
    private String user_account;

    /**
     * 用户头像
     */
    private String avatar_url;

    /**
     * 性别 0-女 1-男 2-保密
     */
    private Integer gender;

    /**
     * 
     */
    private String profile;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 员工状态（0-正常在岗，1-请假离岗，2-未请假离岗，3-离职）
     */
    private Integer status;

    /**
     * 用户角色 0-普通用户,1-管理员
     */
    private Integer role;

    /**
     * 
     */
    private String friend_ids;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 是否删除
     */
    private Integer is_delete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.fishman.usercenter.model.request;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户加入队伍请求体
 *
 * @author fishamn
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = -8462829190623842140L;

    /**
     * id
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;
}

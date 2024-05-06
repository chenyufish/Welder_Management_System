package com.fishman.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户退出队伍请求体
 *
 */
@Data
public class TeamQuitRequest implements Serializable {


    private static final long serialVersionUID = 6953630814613994810L;

    /**
     * id
     */
    private Long teamId;

}

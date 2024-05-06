package com.fishman.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用删除请求
 *
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = 4099065108964587508L;

    private long id;
}

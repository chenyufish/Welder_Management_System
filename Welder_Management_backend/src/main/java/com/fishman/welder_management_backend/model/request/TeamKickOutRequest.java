package com.fishman.welder_management_backend.model.request;

import lombok.Data;

/**
 * 团队退出请求
 *
 * @author fishman
 * @date 2024/01/25
 */
@Data
public class TeamKickOutRequest {
    private Long teamId;
    private Long userId;
}

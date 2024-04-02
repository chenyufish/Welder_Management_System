package com.yupi.usercenter.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 团队封面更新请求
 *
 * @author OchiaMalu
 */
@Data
public class TeamCoverUpdateRequest {
    private Long id;
    private MultipartFile file;
}

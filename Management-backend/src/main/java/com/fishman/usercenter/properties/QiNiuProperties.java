package com.fishman.usercenter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fishman
 */
@Data
@ConfigurationProperties(prefix = "fishman.qiniu")
@Component
public class QiNiuProperties {

    private String accessKey;

    private String secretKey;

    private String bucket;

    private String url;
}

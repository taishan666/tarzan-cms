package com.tarzan.cms.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cms")
public class CmsProperties {
    private String workDir;
    private Integer embeddedRedisPort=6379;
    private String embeddedRedisPassword="123456";
}

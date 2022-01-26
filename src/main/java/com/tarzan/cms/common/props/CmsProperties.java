package com.tarzan.cms.common.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cms")
public class CmsProperties {
    private String themeDir;
    private String backupDir;
    private Boolean embeddedRedisEnabled=true;
    private Integer embeddedRedisPort=6379;
    private String embeddedRedisPassword="123456";
}

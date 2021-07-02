package com.tarzan.cms.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "static")
public class StaticHtmlProperties {

    private String accessPathPattern;

    private String folder;
}

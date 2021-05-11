package com.tarzan.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "static")
public class StaticizeProperties {

    private String accessPathPattern = "/html/**";

    private String folder;
}

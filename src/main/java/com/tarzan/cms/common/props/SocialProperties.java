package com.tarzan.cms.common.props;

import com.google.common.collect.Maps;
import lombok.Data;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "social")
public class SocialProperties {
    private Boolean enabled = false;
    private String domain;
    private Map<AuthDefaultSource, AuthConfig> oauth = Maps.newHashMap();
    private Map<String, String> alias = Maps.newHashMap();
}

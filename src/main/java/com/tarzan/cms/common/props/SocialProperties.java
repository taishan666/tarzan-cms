package com.tarzan.cms.common.props;

import lombok.Data;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tarzan
 */
@Data
@Component
@ConfigurationProperties(prefix = "social")
public class SocialProperties {
    private Boolean enabled = false;
    private String domain;
    private Map<AuthDefaultSource, AuthConfig> oauth = new HashMap<>(5);
    private Map<String, String> alias =  new HashMap<>(5);
}

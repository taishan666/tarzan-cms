package com.tarzan.cms.auth.props;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("blade.token")
@Data
public class JwtProperties {
    private Boolean state;
    private Boolean single;
    private String signKey;

    public JwtProperties() {
        this.state = Boolean.FALSE;
        this.single = Boolean.FALSE;
        this.signKey = "BladeX";
    }
}

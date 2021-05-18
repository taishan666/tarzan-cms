package com.tarzan.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tarzan liu
 * @date 2020/4/18 12:10 下午
 */
@Data
@ConfigurationProperties(prefix = "file")
public class FileUploadProperties {

    private String accessPathPattern;
    private String uploadFolder;
    private String accessPrefixUrl;
}

package com.tarzan.common.config;

import com.tarzan.common.config.properties.FileUploadProperties;
import com.tarzan.common.config.properties.StaticizeProperties;
import com.tarzan.common.intercepter.CommonDataInterceptor;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tarzan liu
 * @date 2020/4/18 11:58 上午
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({FileUploadProperties.class, StaticizeProperties.class})
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileUploadProperties fileUploadProperties;
    private final StaticizeProperties staticizeProperties;
    private final CommonDataInterceptor commonDataInterceptor;

    /**
     * 配置本地文件上传的虚拟路径和静态化的文件生成路径
     * 备注：这是一种图片上传访问图片的方法，实际上也可以使用nginx反向代理访问图片
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 文件上传
        String uploadFolder = fileUploadProperties.getUploadFolder();
        uploadFolder = StringUtils.appendIfMissing(uploadFolder, File.separator);
        registry.addResourceHandler(fileUploadProperties.getAccessPathPattern())
                .addResourceLocations("file:" + uploadFolder);
        // 静态化
        String staticFolder = staticizeProperties.getFolder();
        staticFolder = StringUtils.appendIfMissing(staticFolder, File.separator);
        registry.addResourceHandler(staticizeProperties.getAccessPathPattern())
                .addResourceLocations("file:" + staticFolder);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonDataInterceptor).addPathPatterns("/**");
    }
}

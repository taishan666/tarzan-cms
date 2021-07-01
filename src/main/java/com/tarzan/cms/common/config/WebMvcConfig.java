package com.tarzan.cms.common.config;

import com.tarzan.cms.common.config.properties.FileUploadProperties;
import com.tarzan.cms.common.config.properties.StaticHtmlProperties;
import com.tarzan.cms.common.intercepter.CommonDataInterceptor;
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
@EnableConfigurationProperties({FileUploadProperties.class, StaticHtmlProperties.class})
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileUploadProperties fileUploadProperties;
    private final StaticHtmlProperties staticHtmlProperties;
    private final CommonDataInterceptor commonDataInterceptor;

    /**
     * 配置本地文件上传的虚拟路径和静态化的文件生成路径
     * 备注：这是一种图片上传访问图片的方法，实际上也可以使用nginx反向代理访问图片
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String workDir = this.getClass().getResource("/").getPath();
        workDir="file:" +workDir;
        // 文件上传
        String uploadFolder = fileUploadProperties.getUploadFolder();
        uploadFolder = StringUtils.appendIfMissing(uploadFolder, File.separator);
        registry.addResourceHandler(fileUploadProperties.getAccessPathPattern())
                .addResourceLocations("file:" + uploadFolder);
        // 静态化
        String staticFolder = staticHtmlProperties.getFolder();
        staticFolder = StringUtils.appendIfMissing(staticFolder, File.separator);
        registry.addResourceHandler(staticHtmlProperties.getAccessPathPattern())
                .addResourceLocations("file:" + staticFolder);
        // register /themes/** resource handler.
        registry.addResourceHandler("/theme/**")
                .addResourceLocations( workDir+"templates/theme/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonDataInterceptor).addPathPatterns("/**");
    }
}

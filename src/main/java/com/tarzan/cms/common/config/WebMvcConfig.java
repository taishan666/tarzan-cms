package com.tarzan.cms.common.config;

import com.tarzan.cms.common.handle.CommonDataHandler;
import com.tarzan.cms.common.props.CmsProperties;
import com.tarzan.cms.common.props.FileUploadProperties;
import com.tarzan.cms.common.props.StaticHtmlProperties;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

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
    private final CmsProperties cmsProperties;
    private final CommonDataHandler commonDataInterceptor;

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
        String staticFolder = staticHtmlProperties.getFolder();
        staticFolder = StringUtils.appendIfMissing(staticFolder, File.separator);
        registry.addResourceHandler(staticHtmlProperties.getAccessPathPattern())
                .addResourceLocations("file:" + staticFolder);
        //主题资源
        String themeDir = cmsProperties.getThemeDir();
        themeDir = StringUtils.appendIfMissing(themeDir, File.separator);
        registry.addResourceHandler("/theme/**")
                .addResourceLocations("file:" + themeDir);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonDataInterceptor).addPathPatterns("/**");
    }


    @Bean
    public ClassLoaderTemplateResolver localTemplateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(0);
        resolver.setCheckExistence(true);
        return resolver;
    }




}

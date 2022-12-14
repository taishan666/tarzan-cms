package com.tarzan.cms.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Bean
    public Docket moduleDocket() { return docket("网站接口", "com.tarzan.cms.modules.blog.controller"); }


    private Docket docket(String groupName, String basePackages) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackages))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("泰山CMS 接口文档系统")
                .description("泰山CMS api接口文档系统")
                .license("Powered By Tarzan Liu")
                .licenseUrl("http://127.0.0.1")
                .termsOfServiceUrl("http://127.0.0.1")
                .contact(new Contact("tarzan Liu", "http://127.0.0.1", "1334512682@qq.com"))
                .version("V1.0.0")
                .build();
    }



}


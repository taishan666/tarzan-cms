package com.tarzan.cms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Slf4j
@SpringBootApplication
public class CmsApplication {
    public static void main(String[] args){
        SpringApplication.run(CmsApplication.class, args);
    }
}

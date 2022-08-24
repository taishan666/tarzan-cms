package com.tarzan.cms.common.config;


import com.tarzan.cms.common.props.CmsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Lenovo
 */
@Configuration
@ConditionalOnProperty(prefix = "cms",name = "embedded-redis-enabled", havingValue = "true")
public class EmbeddedRedisConfig {

    private RedisServer redisServer;
    @Resource
    private CmsProperties cmsProperties;


    /**
     * 构造方法之后执行.
     *
     * @throws IOException
     */
    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = RedisServer.builder()
                .port(cmsProperties.getEmbeddedRedisPort()) //端口
                .setting("bind localhost") //绑定ip
                .setting("requirepass "+cmsProperties.getEmbeddedRedisPassword()) //设置密码
              //  .setting("maxheap 300m")
                .build();
        redisServer.start();
    }

    /**
     * 析构方法之后执行.
     */
    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}

package com.tarzan.cms.common.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@ConditionalOnProperty(name = "embedded.redis.enable", havingValue = "true")
@Configuration
public class EmbeddedRedisConfig {

    private RedisServer redisServer;

    /**
     * 构造方法之后执行.
     *
     * @throws IOException
     */
    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = RedisServer.builder()
                .port(6379) //端口
                .setting("bind localhost") //绑定ip
                .setting("requirepass 123456") //设置密码
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

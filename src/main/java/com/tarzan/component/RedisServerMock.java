package com.tarzan.component;



import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
public class RedisServerMock {

    private RedisServer redisServer;

    /**
     * 构造方法之后执行.
     *
     * @throws IOException
     */
    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = new RedisServer(6379);
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

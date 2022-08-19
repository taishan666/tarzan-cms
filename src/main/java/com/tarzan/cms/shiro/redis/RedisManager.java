package com.tarzan.cms.shiro.redis;

import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.*;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author tarzan
 */
@Getter
@Setter
public class RedisManager extends WorkAloneRedisManager {

    private static final String DEFAULT_HOST = "127.0.0.1:6379";
    private String host = DEFAULT_HOST;

    /** timeout for jedis try to connect to redis server, not expire time! In milliseconds */
    private int timeout = Protocol.DEFAULT_TIMEOUT;

    private String password;

    private int database = Protocol.DEFAULT_DATABASE;

    private JedisPool jedisPool;

    private void init() {
        synchronized (this) {
            if (jedisPool == null) {
                String[] hostAndPort = host.split(":");
                jedisPool = new JedisPool(getJedisPoolConfig(), hostAndPort[0], Integer.parseInt(hostAndPort[1]), timeout, password, database);
            }
        }
    }

    @Override
    protected Jedis getJedis() {
        if (jedisPool == null) {
            init();
        }
        return jedisPool.getResource();
    }

}

package com.tarzan.cms.auth.cache;

import com.tarzan.cms.utils.RedisUtil;
import com.tarzan.cms.utils.SpringUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *  重试限制缓存
 * @version 1.0
 * @since JDK1.8
 * @author tarzan
 * @date 2022年01月28日 17:13:14
 */
public class RetryLimitCache {

    private static final RedisUtil REDIS_UTIL;

    static {
        REDIS_UTIL = SpringUtil.getBean(RedisUtil.class);
    }

    public static void put (String userName,AtomicInteger retryNum,Long expire) {
            REDIS_UTIL.hSet("password_retry_cache", userName,retryNum);
            REDIS_UTIL.expire("password_retry_cache",expire);
    }

    public static AtomicInteger get (String userName) {
        return REDIS_UTIL.hGet("password_retry_cache", userName, AtomicInteger.class);
    }

    public static void remove (String userName) {
        REDIS_UTIL.hDel("password_retry_cache", userName);
    }

}

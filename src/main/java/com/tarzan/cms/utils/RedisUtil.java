package com.tarzan.cms.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RedisUtil {
    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);
    private static final String SUCCESS = "OK";
    private static final long UN_EXPIRE = -1L;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        Assert.notNull(redisTemplate, "redisTemplate is null");
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, Object value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    public void setEx(String key, Object value, Duration timeout) {
        this.redisTemplate.opsForValue().set(key, value, timeout);
    }

    public void setEx(String key, Object value, Long seconds) {
        this.redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    @Nullable
    public <T> T get(String key) {
        return (T) this.redisTemplate.opsForValue().get(key);
    }

    public <T> T get(String key, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(key)) {
                return null;
            } else {
                Object obj = this.redisTemplate.opsForValue().get(key);
                return (T) obj;
            }
        } catch (Exception var5) {
            log.error("redisUtil get", var5);
            return null;
        }
    }

    @Nullable
    public <T> T get(String key, Supplier<T> loader) {
        T value = this.get(key);
        if (Objects.isNull(value)) {
            return value;
        } else {
            value = loader.get();
            if (Objects.nonNull(value)) {
                this.set(key, value);
            }

            return value;
        }
    }

    public Boolean del(String key) {
        return this.redisTemplate.delete(key);
    }

    public Long del(String... keys) {
        return this.del((Collection)Arrays.asList(keys));
    }

    public Long del(Collection<String> keys) {
        return this.redisTemplate.delete(keys);
    }

    public Set<String> keys(String pattern) {
        return this.redisTemplate.keys(pattern);
    }

    public void multiSet(Map<String, Object> kvMap) {
        this.redisTemplate.opsForValue().multiSet(kvMap);
    }

    public List<Object> multiGet(String... keys) {
        return this.multiGet((Collection)Arrays.asList(keys));
    }

    public List<Object> multiGet(Collection<String> keys) {
        return this.redisTemplate.opsForValue().multiGet(new HashSet(keys));
    }

    public Long decr(String key) {
        return this.redisTemplate.opsForValue().decrement(key);
    }

    public Long decrBy(String key, long longValue) {
        return this.redisTemplate.opsForValue().decrement(key, longValue);
    }

    public Long incr(String key) {
        return this.redisTemplate.opsForValue().increment(key);
    }

    public Long incrBy(String key, long longValue) {
        return this.redisTemplate.opsForValue().increment(key, longValue);
    }

    public Long getCounter(String key) {
        return (Long)this.redisTemplate.opsForValue().get(key);
    }

    public Boolean exists(String key) {
        return this.redisTemplate.hasKey(key);
    }

    public String randomKey() {
        return (String)this.redisTemplate.randomKey();
    }

    public void rename(String oldkey, String newkey) {
        this.redisTemplate.rename(oldkey, newkey);
    }

    public Boolean move(String key, int dbIndex) {
        return this.redisTemplate.move(key, dbIndex);
    }

    public Boolean expire(String key, long seconds) {
        return this.redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public Boolean expire(String key, Duration timeout) {
        return this.expire(key, timeout.getSeconds());
    }

    public Boolean expireAt(String key, Date date) {
        return this.redisTemplate.expireAt(key, date);
    }

    public Boolean expireAt(String key, long unixTime) {
        return this.expireAt(key, new Date(unixTime));
    }

    public Boolean pexpire(String key, long milliseconds) {
        return this.redisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);
    }

    public <T> T getSet(String key, Object value) {
        return (T) this.redisTemplate.opsForValue().getAndSet(key, value);
    }

    public Boolean persist(String key) {
        return this.redisTemplate.persist(key);
    }

    public String type(String key) {
        if (StringUtil.isBlank(key)) {
            return "";
        } else {
            DataType dataType = this.redisTemplate.type(key);
            return Objects.nonNull(dataType) ? dataType.code() : "";
        }
    }

    public Long ttl(String key) {
        return this.redisTemplate.getExpire(key);
    }

    public Long pttl(String key) {
        return this.redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    public void hSet(String key, Object field, Object value) {
        this.redisTemplate.opsForHash().put(key, field, value);
    }

    public void hMset(String key, Map<? extends Object, ? extends Object> hash) {
        this.redisTemplate.opsForHash().putAll(key, hash);
    }

    public <T> T hGet(String key, Object field) {
        return (T) this.redisTemplate.opsForHash().get(key, field);
    }

    public <T> T hGet(String key, Serializable item, Class<T> clazz) {
        T t = null;
        if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(item)) {
            Object obj = this.redisTemplate.opsForHash().get(key, item);
            if (obj == null) {
                return null;
            } else {
                try {
                    t = (T) obj;
                } catch (Exception var7) {
                    log.error("RedisUtil.hget", var7);
                }

                return t;
            }
        } else {
            return null;
        }
    }

    public <T> T hGet(String key, Serializable item, Class<T> clazz, Supplier<T> supplier) {
        if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(item)) {
            T t = (T) this.redisTemplate.opsForHash().get(key, item);
            if (Objects.isNull(t)) {
                t = supplier.get();
                if (Objects.nonNull(t)) {
                    this.hSet(key, item, t);
                }
            }

            return t;
        } else {
            return null;
        }
    }

    public <T> T hGet(String key, Serializable item, Supplier<T> supplier) {
        if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(item)) {
            T t = (T) this.redisTemplate.opsForHash().get(key, item);
            if (Objects.isNull(t)) {
                t = supplier.get();
                if (Objects.nonNull(t)) {
                    this.hSet(key, item, t);
                }
            }

            return t;
        } else {
            return null;
        }
    }

    public List hmGet(String key, Object... fields) {
        return this.hmGet(key, (Collection)Arrays.asList(fields));
    }

    public List hmGet(String key, Collection<Object> hashKeys) {
        return this.redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    public Long hDel(String key, Object... fields) {
        return this.redisTemplate.opsForHash().delete(key, fields);
    }

    public Long hDel(String key, Collection<Object> fields) {
        return this.redisTemplate.opsForHash().delete(key, new Object[]{fields});
    }

    public Boolean hExists(String key, Object field) {
        return this.redisTemplate.opsForHash().hasKey(key, field);
    }

    public List hVals(String key) {
        return this.redisTemplate.opsForHash().values(key);
    }

    public Set<Object> hKeys(String key) {
        return this.redisTemplate.opsForHash().keys(key);
    }

    public Long hLen(String key) {
        return this.redisTemplate.opsForHash().size(key);
    }

    public Long hIncrBy(String key, Object field, long value) {
        return this.redisTemplate.opsForHash().increment(key, field, value);
    }

    public Double hIncrByFloat(String key, Object field, double value) {
        return this.redisTemplate.opsForHash().increment(key, field, value);
    }

    public <T> T lIndex(String key, long index) {
        return (T) this.redisTemplate.opsForList().index(key, index);
    }

    public Long lLen(String key) {
        return this.redisTemplate.opsForList().size(key);
    }

    public <T> T lPop(String key) {
        return (T) this.redisTemplate.opsForList().leftPop(key);
    }

    public Long lPush(String key, Object... values) {
        return this.redisTemplate.opsForList().leftPush(key, values);
    }

    public void lSet(String key, long index, Object value) {
        this.redisTemplate.opsForList().set(key, index, value);
    }

    public Long lDel(String key, long count, Object value) {
        return this.redisTemplate.opsForList().remove(key, count, value);
    }

    public Long lDelOne(String key, Object value) {
        return StringUtil.isBlank(key) ? null : this.lDel(key, 1L, value);
    }

    public Long lDelAll(String key, Object value) {
        return this.lDel(key, 0L, value);
    }

    public List lRange(String key, long start, long end) {
        return this.redisTemplate.opsForList().range(key, start, end);
    }

    public void lTrim(String key, long start, long end) {
        this.redisTemplate.opsForList().trim(key, start, end);
    }

    public <T> T rPop(String key) {
        return (T) this.redisTemplate.opsForList().rightPop(key);
    }

    public Long rPush(String key, Object... values) {
        return this.redisTemplate.opsForList().rightPush(key, values);
    }

    public <T> T rPopLPush(String srcKey, String dstKey) {
        return (T) this.redisTemplate.opsForList().rightPopAndLeftPush(srcKey, dstKey);
    }

    public Long sAdd(String key, Object... members) {
        return this.redisTemplate.opsForSet().add(key, members);
    }

    public <T> T sPop(String key) {
        return StringUtil.isBlank(key) ? null : (T) this.redisTemplate.opsForSet().pop(key);
    }

    public Set sMembers(String key) {
        return StringUtil.isBlank(key) ? Collections.emptySet() : this.redisTemplate.opsForSet().members(key);
    }

    public <T> Set<T> sMembers(String key, Class<T> clazz) {
        if (StringUtil.isBlank(key)) {
            return Collections.emptySet();
        } else {
            Set<?> set = this.redisTemplate.opsForSet().members(key);
            return CollectionUtils.isEmpty(set) ? Collections.emptySet() : (Set)set.stream().map((item) -> {
                return item;
            }).collect(Collectors.toSet());
        }
    }

    public boolean sIsMember(String key, Object member) {
        return !StringUtil.isBlank(key) && !Objects.isNull(member) ? this.redisTemplate.opsForSet().isMember(key, member) : false;
    }

    public Set sInter(String key, String otherKey) {
        return this.redisTemplate.opsForSet().intersect(key, otherKey);
    }

    public Set sInter(String key, Collection<String> otherKeys) {
        return this.redisTemplate.opsForSet().intersect(key, otherKeys);
    }

    public <T> T sRandMember(String key) {
        return (T) this.redisTemplate.opsForSet().randomMember(key);
    }

    public Set sUnion(String key, String otherKey) {
        return this.redisTemplate.opsForSet().union(key, otherKey);
    }

    public Set sUnion(String key, Collection<String> otherKeys) {
        return this.redisTemplate.opsForSet().union(key, otherKeys);
    }

    public Set sDiff(String key, String otherKey) {
        return this.redisTemplate.opsForSet().difference(key, otherKey);
    }

    public Set sDiff(String key, Collection<String> otherKeys) {
        return this.redisTemplate.opsForSet().difference(key, otherKeys);
    }

    public boolean hCover(String key, Map<? extends Serializable, Object> map) {
        if (!StringUtil.isBlank(key) && !Objects.isNull(map)) {
            String result = (String)this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
                try {
                    connection.multi();
                    StringRedisSerializer srs = (StringRedisSerializer)this.redisTemplate.getKeySerializer();
                    byte[] rawKey = srs.serialize(key);
                    if (Objects.isNull(rawKey)) {
                        return null;
                    }

                    connection.del(new byte[][]{rawKey});
                    Map<byte[], byte[]> hashes = new LinkedHashMap(map.size());
                    GenericJackson2JsonRedisSerializer hashKeySerializer = (GenericJackson2JsonRedisSerializer)this.redisTemplate.getHashKeySerializer();
                    GenericJackson2JsonRedisSerializer hashValueSerializer = (GenericJackson2JsonRedisSerializer)this.redisTemplate.getHashValueSerializer();
                    Iterator var9 = map.entrySet().iterator();

                    while(var9.hasNext()) {
                        Entry<? extends Serializable, Object> entry = (Entry)var9.next();
                        hashes.put(hashKeySerializer.serialize(entry.getKey()), hashValueSerializer.serialize(entry.getValue()));
                    }

                    connection.hMSet(rawKey, hashes);
                    connection.exec();
                } catch (SerializationException var11) {
                    connection.discard();
                }

                return null;
            });
            return Objects.equals("OK", result);
        } else {
            return false;
        }
    }

    public void hdel(String key, Object... item) {
        this.redisTemplate.opsForHash().delete(key, item);
    }

    public <T, HK, HV> boolean hSetBatch(List<T> list, Function<T, String> keyFunction, Function<T, HK> hashKeyFunction, Function<T, HV> hashValueFunction) {
        RedisConnection connection = null;

        boolean var6;
        try {
            if (!CollectionUtils.isEmpty(list) && !Objects.isNull(keyFunction) && !Objects.isNull(hashKeyFunction) && !Objects.isNull(hashValueFunction)) {
                connection = ((RedisConnectionFactory)Objects.requireNonNull(this.redisTemplate.getConnectionFactory())).getConnection();
                StringRedisSerializer srs = (StringRedisSerializer)this.redisTemplate.getKeySerializer();
                GenericJackson2JsonRedisSerializer hashKeySerializer = (GenericJackson2JsonRedisSerializer)this.redisTemplate.getHashKeySerializer();
                GenericJackson2JsonRedisSerializer hashValueSerializer = (GenericJackson2JsonRedisSerializer)this.redisTemplate.getHashValueSerializer();
                connection.openPipeline();
                Iterator var9 = list.iterator();

                while(var9.hasNext()) {
                    T t = (T) var9.next();
                    connection.hSet((byte[])Objects.requireNonNull(srs.serialize((String)keyFunction.apply(t))), (byte[])Objects.requireNonNull(hashKeySerializer.serialize(hashKeyFunction.apply(t))), (byte[])Objects.requireNonNull(hashValueSerializer.serialize(hashValueFunction.apply(t))));
                }

                connection.closePipeline();
                boolean var18 = true;
                return var18;
            }

            var6 = false;
        } catch (Exception var14) {
            boolean var7 = false;
            return var7;
        } finally {
            if (Objects.nonNull(connection) && !connection.isClosed()) {
                if (connection.isPipelined()) {
                    connection.closePipeline();
                }

                RedisConnectionUtils.releaseConnection(connection, this.redisTemplate.getConnectionFactory());
            }

        }

        return var6;
    }

    public boolean hDelBatch(List<String> keys, Object item) {
        RedisConnection connection = ((RedisConnectionFactory)Objects.requireNonNull(this.redisTemplate.getConnectionFactory())).getConnection();
        StringRedisSerializer srs = (StringRedisSerializer)this.redisTemplate.getKeySerializer();
        GenericJackson2JsonRedisSerializer hashKeySerializer = (GenericJackson2JsonRedisSerializer)this.redisTemplate.getHashKeySerializer();
        connection.openPipeline();
        Iterator var6 = keys.iterator();

        while(var6.hasNext()) {
            String key = (String)var6.next();
            connection.hDel((byte[])Objects.requireNonNull(srs.serialize(key)), new byte[][]{hashKeySerializer.serialize(item)});
        }

        connection.closePipeline();
        return true;
    }

    public boolean setNxPx(String key, String val, long expire) {
        if (StringUtil.isAnyBlank(new CharSequence[]{key, val})) {
            return false;
        } else {
            try {
                RedisConnection conn = this.redisTemplate.getConnectionFactory().getConnection();
                byte[] rawKeys = key.getBytes(StandardCharsets.UTF_8);
                byte[] rawValues = val.getBytes(StandardCharsets.UTF_8);
                return conn.set(rawKeys, rawValues, Expiration.from(expire, TimeUnit.MILLISECONDS), SetOption.SET_IF_ABSENT);
            } catch (Exception var8) {
                log.error("set nx px error ", var8);
                return false;
            }
        }
    }

    public <HK, HV> Map<HK, HV> hgetAll(String key, Class<HK> hkClazz, Class<HV> hvClazz) {
        try {
            Map<Object, Object> data = this.redisTemplate.opsForHash().entries(key);
            if (CollectionUtils.isEmpty(data)) {
                return Collections.emptyMap();
            } else {
                Map<HK, HV> result = new LinkedHashMap();
                Iterator var6 = data.entrySet().iterator();

                while(var6.hasNext()) {
                    Entry<HK, HV> entry = (Entry)var6.next();
                    result.put(entry.getKey(), entry.getValue());
                }
                return result;
            }
        } catch (Exception var8) {
            return Collections.emptyMap();
        }
    }

    public <T> Map<T, T> hgetAll(String key, Class<T> clazz) {
        try {
            Map<Object, Object> data = this.redisTemplate.opsForHash().entries(key);
            if (CollectionUtils.isEmpty(data)) {
                return Collections.emptyMap();
            } else {
                Map<T, T> result = new LinkedHashMap();
                Iterator var5 = data.entrySet().iterator();

                while(var5.hasNext()) {
                    Entry<T, T> entry = (Entry)var5.next();
                    result.put(entry.getKey(), entry.getValue());
                }
                return result;
            }
        } catch (Exception var7) {
            return Collections.emptyMap();
        }
    }

    public boolean setNxPx(String key, String val) {
        return this.setNxPx(key, val, -1L);
    }

    public <E> List<E> lGetAll(String key, Class<E> clazz) {
        if (StringUtil.isBlank(key)) {
            return Collections.emptyList();
        } else {
            try {
                long size = this.redisTemplate.opsForList().size(key);
                if (size <= 0L) {
                    return Collections.emptyList();
                } else {
                    List<?> list = this.redisTemplate.opsForList().range(key, 0L, size);
                    return CollectionUtils.isEmpty(list) ? Collections.emptyList() : (List)list.stream().map((item) -> {
                        return item;
                    }).collect(Collectors.toList());
                }
            } catch (Exception var6) {
                log.error("RedisUtil.lGet", var6);
                return null;
            }
        }
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return this.redisTemplate;
    }
}

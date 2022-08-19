package com.tarzan.cms.shiro.serializer;


import com.tarzan.cms.shiro.exception.SerializationException;

/**
 * @author tarzan
 */
public interface RedisSerializer<T> {

    byte[] serialize(T t) throws SerializationException;

    T deserialize(byte[] bytes) throws SerializationException;
}


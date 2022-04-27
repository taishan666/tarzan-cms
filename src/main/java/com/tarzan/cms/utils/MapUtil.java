package com.tarzan.cms.utils;

import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapUtil {
    public MapUtil() {
    }

    public static <T, K, U> Map<K, U> map(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, List<T> sourceList) {
        return CollectionUtils.isEmpty(sourceList) ? Collections.emptyMap() : (Map)sourceList.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public static <T, K> Map<K, T> map(Function<? super T, ? extends K> keyMapper, Collection<T> sourceList) {
        return CollectionUtils.isEmpty(sourceList) ? Collections.emptyMap() : (Map)sourceList.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    public static <T, R> List<R> mapList(Function<? super T, ? extends R> keyMapper, List<T> sourceList) {
        return CollectionUtils.isEmpty(sourceList) ? Collections.emptyList() : (List)sourceList.stream().map(keyMapper).collect(Collectors.toList());
    }

    public static <T> List<T> mapList(List<T> sourceList) {
        return CollectionUtils.isEmpty(sourceList) ? Collections.emptyList() : (List)sourceList.stream().map(Function.identity()).collect(Collectors.toList());
    }

    public static <T, K> Map<K, List<T>> group(Function<? super T, ? extends K> keyMapper, Collection<T> sourceList) {
        return CollectionUtils.isEmpty(sourceList) ? Collections.emptyMap() : (Map)sourceList.stream().collect(Collectors.groupingBy(keyMapper));
    }

    public static <T, K, U> Map<K, List<U>> group(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, List<T> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyMap();
        } else {
            Map<K, List<U>> dd = (Map)sourceList.stream().collect(Collectors.groupingBy(keyMapper, HashMap::new, Collectors.mapping(valueMapper, Collectors.toList())));
            return dd;
        }
    }

    public static <T, K, U> Map<K, Set<U>> groupToSet(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, List<T> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyMap();
        } else {
            Map<K, Set<U>> dd = (Map)sourceList.stream().collect(Collectors.groupingBy(keyMapper, HashMap::new, Collectors.mapping(valueMapper, Collectors.toSet())));
            return dd;
        }
    }
}

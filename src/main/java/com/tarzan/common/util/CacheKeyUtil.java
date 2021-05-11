package com.tarzan.common.util;

import com.alibaba.fastjson.JSON;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;
import org.springframework.validation.support.BindingAwareModelMap;

/**
 * 缓存key工具类
 *
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@UtilityClass
public class CacheKeyUtil {

    /**
     * 获取方法参数组成的key
     *
     * @param params 参数数组
     */
    public static String getMethodParamsKey(Object... params) {
        if (StringUtils.isEmpty(params)) {
            return "";
        }
        StringBuilder key = new StringBuilder("(");
        for (Object obj : params) {
            if (obj.getClass().equals(BindingAwareModelMap.class)) {
                continue;
            }
            key.append(JSON.toJSONString(obj).replaceAll("\"", "'"));
        }
        key.append(')');
        return key.toString();
    }

}

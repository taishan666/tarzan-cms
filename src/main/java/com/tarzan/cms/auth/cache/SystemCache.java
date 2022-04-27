package com.tarzan.cms.auth.cache;

import com.tarzan.cms.auth.constant.CacheConstant;
import com.tarzan.cms.modules.admin.model.sys.User;
import com.tarzan.cms.modules.admin.service.sys.UserService;
import com.tarzan.cms.utils.MapUtil;
import com.tarzan.cms.utils.RedisUtil;
import com.tarzan.cms.utils.SpringUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 系统通用缓存（菜单、部门、岗位、角色、租户）
 *
 * @author tarizan
 * @version 1.0
 * @date 2020年09月18日 14:59:03
 * @since JDK1.8
 */
public class SystemCache {

    private static final RedisUtil REDIS_UTIL;
    private static final UserService USER_SERVICE;

    static {
        REDIS_UTIL = SpringUtil.getBean(RedisUtil.class);
        USER_SERVICE = SpringUtil.getBean(UserService.class);
    }

    /**
     * 获取用户
     *
     * @param userId 用户id
     * @return
     */
    public static User getUser (Long userId) {
        return REDIS_UTIL.hGet(CacheConstant.USER_CACHE, userId, User.class, () -> USER_SERVICE.getById(userId));
    }

    /**
     * 获取所有用户
     *
     * @return
     */
    public static Collection<User> getAllUser () {
        return REDIS_UTIL.hgetAll(CacheConstant.USER_CACHE, Long.class, User.class).values();
    }

    /**
     * 删除用户
     *
     * @param userIds 用户id列表
     * @return
     */
    public static void delUser (List<Long> userIds) {
        REDIS_UTIL.hDel(CacheConstant.USER_CACHE, userIds);
        REDIS_UTIL.hDel(CacheConstant.USER_NAME_CACHE, userIds);
    }

    public static void delAllUser(){
        REDIS_UTIL.del(CacheConstant.USER_CACHE);
    }

    /**
     * 删除一个用户
     *
     * @param userId 用户id
     * @return
     */
    public static void delOneUser (Long userId) {
        REDIS_UTIL.hDel(CacheConstant.USER_CACHE, userId);
        REDIS_UTIL.hDel(CacheConstant.USER_NAME_CACHE, userId);
    }

    /**
     * 保存用户
     *
     * @param user 用户信息
     * @return
     */
    public static void saveUser (User user) {
        REDIS_UTIL.hSet(CacheConstant.USER_CACHE, user.getId(), user);
        REDIS_UTIL.hSet(CacheConstant.USER_NAME_CACHE, user.getId(), user.getRealName());
    }

    public static void initUser () {
        List<User> list = USER_SERVICE.list();
        REDIS_UTIL.hMset(CacheConstant.USER_CACHE, (Map) MapUtil.map(User::getId, list));
        REDIS_UTIL.hMset(CacheConstant.USER_NAME_CACHE, MapUtil.map(User::getId, User::getRealName, list));
    }

}

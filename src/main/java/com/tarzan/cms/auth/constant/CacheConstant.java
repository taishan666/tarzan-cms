package com.tarzan.cms.auth.constant;

/**
 * 统一数据库 缓存常量
 *
 * @author tarzan
 * @version 1.0
 * @date 2021年08月06日 10:38:22
 */
public interface CacheConstant {
    /** 用户缓存 */
    String USER_CACHE = "user";
    /** 用户ID-NAME 缓存 */
    String USER_NAME_CACHE = "user_name";
    /** 登录验证码 缓存 */
    String CAPTCHA_KEY = "auth:captcha:";
    /** 任务缓存 缓存 */
    String COMPILE_TASK_CACHE = "compile_task";

}

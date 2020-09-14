package com.store.common.util;

/**
 * 功能：生产redis key工具类
 * @author sunpeng
 * @date 2018
 */
public class RedisKeyUtil {
    // 获取userId 对应redis中用户信息key
    public static String getRedisUserInfoKey(Object userId) {
        return "user_id_" + userId;
    }
}
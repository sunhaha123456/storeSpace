package com.store.service;

import com.store.common.data.base.PageList;
import com.store.data.entity.TbSysUser;
import com.store.data.to.request.UserSaveRequest;
import com.store.data.to.request.UserSearchRequest;
import com.store.data.to.vo.UserRedisVo;

/**
 * 功能：user service
 * @author sunpeng
 * @date 2018
 */
public interface UserService {
    PageList<TbSysUser> search(UserSearchRequest param) throws Exception;
    void save(UserSaveRequest param) throws Exception;
    void opert(Long id, Integer state);
    TbSysUser getDetail(Long id);
    void userRedisInfoSave(String redisKey, UserRedisVo userRedis);
    void updatePasswodAdmin(Long id, String userNewPassword) throws Exception;
    void updatePasswodSelf(String userOldPassword, String userNewPassword) throws Exception;
}
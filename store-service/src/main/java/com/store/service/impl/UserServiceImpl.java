package com.store.service.impl;

import com.store.common.data.base.PageList;
import com.store.common.data.response.ResponseResultCode;
import com.store.common.exception.BusinessException;
import com.store.common.repository.RedisRepositoryCustom;
import com.store.common.util.*;
import com.store.data.constant.SystemConstant;
import com.store.data.entity.TbRoleGroup;
import com.store.data.entity.TbSysUser;
import com.store.data.to.request.UserSaveRequest;
import com.store.data.to.request.UserSearchRequest;
import com.store.data.to.vo.UserRedisVo;
import com.store.repository.RoleGroupRepository;
import com.store.repository.SysUserRepository;
import com.store.repository.SysUserRepositoryCustom;
import com.store.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Inject
    private SysUserRepositoryCustom sysUserRepositoryCustom;
    @Inject
    private SysUserRepository sysUserRepository;
    @Inject
    private RoleGroupRepository roleGroupRepository;
    @Inject
    private RedisRepositoryCustom redisRepositoryCustom;
    @Inject
    private ValueHolder valueHolder;

    @Override
    public PageList<TbSysUser> search(UserSearchRequest param) throws Exception {
        return sysUserRepositoryCustom.list(param.getLoginName(), param.getPage(), param.getRows());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(UserSaveRequest param) throws Exception {
        Long id = param.getId();
        if (id == null) { // 新增
            long c = sysUserRepository.countByLoginName(param.getLoginName());
            if (0 < c) {
                throw new BusinessException("用户名重复！");
            }
            TbSysUser user = new TbSysUser();
            BeanUtils.copyProperties(param, user);
            Date now = new Date();
            user.setId(null);
            user.setCreateDate(now);
            user.setLastModified(now);
            user.setUserState(0);
            user.setUpwd(Md5Util.MD5Encode(user.getUpwd()));
            sysUserRepository.save(user);
        } else { // 修改
            Long roleId = param.getRoleGroupId();
            String userName = param.getUserName();

            TbSysUser user = sysUserRepository.findOne(id);
            if (user == null) {
                throw new BusinessException("该用户不存在！");
            }
            if (user.getUserState() == 2) {
                throw new BusinessException("查无此用户！");
            }
            if (user.getUserState() != 0) {
                throw new BusinessException("该用户已被冻结！");
            }
            TbRoleGroup role = roleGroupRepository.findOne(roleId);
            if (role == null) {
                throw new BusinessException("角色组不存在！");
            }
            if (role.getRoleState() != 0) {
                throw new BusinessException("该角色组已被冻结！");
            }
            int c = sysUserRepository.updateRoleAndUserName(id, roleId, userName);
            if (c <= 0) {
                throw new BusinessException(ResponseResultCode.OPERT_ERROR);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void opert(Long id, Integer state) {
        TbSysUser sysUser = sysUserRepository.findOne(id);
        if (sysUser == null) {
            throw new BusinessException("查无此用户！");
        }
        if (sysUser.getUserState() == 2) {
            log.error("【接口 -user/userManage/opert】【该用户已被注销！】【userId：{}】", sysUser.getId());
            throw new BusinessException("查无此用户！");
        }
        if (sysUser.getUserState().equals(state)) {
            if (state == 0) {
                throw new BusinessException("此用户状态原已正常！");
            } else {
                throw new BusinessException("此用户状态原已被冻结！");
            }
        }
        if (state == 2) {
            if (sysUser.getUserState() != 1) {
                throw new BusinessException("注销前请先冻结该用户！");
            }
        }
        int c = sysUserRepository.updateStateById(id, state);
        if (c <= 0) {
            throw new BusinessException(ResponseResultCode.OPERT_ERROR);
        }
        if (state != 0) {
            // 更新redis
            String userInfoKey = RedisKeyUtil.getRedisUserInfoKey(sysUser.getId());
            redisRepositoryCustom.delete(userInfoKey);
        }
    }

    @Override
    public TbSysUser getDetail(Long id) {
        TbSysUser user = sysUserRepository.findOne(id);
        if (user == null) {
            throw new BusinessException("查无此用户！");
        }
        if (user.getUserState() == 2) {
            log.error("【接口 -user/userManage/getDetail】【该用户已被注销！】【userId：{}】", id);
            throw new BusinessException("查无此用户！");
        }
        return user;
    }

    public void userRedisInfoSave(String redisKey, UserRedisVo userRedis) {
        redisRepositoryCustom.saveMinutes(redisKey, JsonUtil.objectToJson(userRedis), SystemConstant.TOKEN_SAVE_TIME);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePasswodAdmin(Long id, String userNewPassword) throws Exception {
        int c = sysUserRepository.updatePasswod(id, Md5Util.MD5Encode(userNewPassword));
        if (c <= 0) {
            throw new BusinessException(ResponseResultCode.OPERT_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePasswodSelf(String userOldPassword, String userNewPassword) throws Exception {
        Long userId = valueHolder.getUserIdHolder();
        TbSysUser user = sysUserRepository.findOne(userId);
        if (user == null) {
            throw new BusinessException(ResponseResultCode.LOGIN_FIRST);
        }
        userOldPassword = Md5Util.MD5Encode(userOldPassword);
        userNewPassword = Md5Util.MD5Encode(userNewPassword);
        if (!userOldPassword.equals(user.getUpwd())) {
            throw new BusinessException(ResponseResultCode.USER_OLD_PASSWORD_ERROR);
        }
        int c = sysUserRepository.updatePasswodByOldPassword(userId, userOldPassword, userNewPassword);
        if (c <= 0) {
            throw new BusinessException(ResponseResultCode.OPERT_ERROR);
        }
    }
}
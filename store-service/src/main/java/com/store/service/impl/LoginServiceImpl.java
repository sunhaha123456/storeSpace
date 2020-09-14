package com.store.service.impl;

import com.store.common.data.response.ResponseResultCode;
import com.store.common.exception.BusinessException;
import com.store.common.repository.RedisRepositoryCustom;
import com.store.common.util.*;
import com.store.data.constant.SystemConstant;
import com.store.data.entity.TbRoleGroup;
import com.store.data.entity.TbSysUser;
import com.store.data.to.request.UserLoginRequest;
import com.store.data.to.vo.UserRedisVo;
import com.store.repository.RoleGroupRepository;
import com.store.repository.SysUserRepository;
import com.store.service.LoginService;
import com.store.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Inject
    private SysUserRepository sysUserRepository;
    @Inject
    private RoleGroupRepository roleGroupRepository;
    @Inject
    private RedisRepositoryCustom redisRepositoryCustom;
    @Inject
    private UserService userService;
    @Inject
    private ValueHolder valueHolder;

    @Override
    public Map<String, Object> verify(UserLoginRequest user) throws Exception {
        // 1、校验验证码
        String codeRedis = redisRepositoryCustom.getString(SystemConstant.LOGIN_CODE_PREFIX + user.getKey());
        String codeFront = Md5Util.MD5Encode(user.getCode());
        if (StringUtil.isEmpty(codeRedis) || StringUtil.isEmpty(codeFront) || !codeRedis.equals(codeFront)) {
            throw new BusinessException(ResponseResultCode.CODE_ERROR);
        }
        // 2、校验用户名和密码，并且用户状态正常
        TbSysUser sysUser = sysUserRepository.findByLoginNameAndUpwd(user.getLoginName(), Md5Util.MD5Encode(user.getUpwd()));
        if (sysUser == null) {
            throw new BusinessException(ResponseResultCode.LOGIN_ERROR);
        }
        if (!Arrays.asList(0, 1, 2).contains(sysUser.getUserState())) {
            throw new BusinessException(ResponseResultCode.USER_STATE_ERROR);
        }
        if (sysUser.getUserState() == 1) {
            throw new BusinessException("用户已被冻结！");
        }
        if (sysUser.getUserState() == 2) {
            throw new BusinessException("用户已被注销！");
        }
        TbRoleGroup role = roleGroupRepository.findOne(sysUser.getRoleGroupId());
        if (role == null) {
            log.error("【接口 -/login/verify】【登录失败，用户所属角色组未查找到！】【用户信息：{}】", JsonUtil.objectToJson(sysUser));
            throw new BusinessException("用户信息异常！");
        }
        if (role.getRoleState() != 0) {
            throw new BusinessException("用户所属角色组已被冻结！");
        }
        // 3、更新redis用户信息，更新用户token、用户状态
        UserRedisVo userRedis = new UserRedisVo(IdUtil.getID() + IdUtil.getID());
        userService.userRedisInfoSave(RedisKeyUtil.getRedisUserInfoKey(sysUser.getId()), userRedis);
        // 4、删除redis验证码
        redisRepositoryCustom.delete(SystemConstant.LOGIN_CODE_PREFIX + user.getKey());
        Map<String, Object> res = new HashMap();
        res.put("token", userRedis.getToken());
        res.put("userId", sysUser.getId());
        return res;
    }

    @Override
    public void out() throws Exception {
        redisRepositoryCustom.delete(valueHolder.getTokenHolder());
    }

    @Override
    public boolean tokenValidate(HttpServletRequest request) {
        String method = request.getMethod();
        if ("OPTIONS".equals(method.toUpperCase())) {
            return true;
        }
        String token = request.getHeader(SystemConstant.SYSTEM_TOKEN_NAME);
        String userId = request.getHeader(SystemConstant.SYSTEM_USER_ID);
        if (StringUtil.isEmpty(token)) {
            token = request.getParameter(SystemConstant.SYSTEM_TOKEN_NAME);
        }
        if (StringUtil.isEmpty(userId)) {
            userId = request.getParameter(SystemConstant.SYSTEM_USER_ID);
        }
        String url = request.getRequestURI();
        if (StringUtil.isEmpty(token) || token.length() != 64 || StringUtil.isEmpty(userId)) {
            log.error("Request url：{}，method：{}，userId：{}，token：{}，拦截此请求：001-请求不合法！", url, method, userId, token);
            return false;
        }
        UserRedisVo userRedis = redisRepositoryCustom.getClassObj(RedisKeyUtil.getRedisUserInfoKey(userId), UserRedisVo.class);
        if (userRedis == null) {
            log.error("Request url：{}，method：{}，userId：{}，token：{}，拦截此请求：002-redis中userId对应键值已超时！", url, method, userId, token);
            return false;
        }
        if (!token.equals(userRedis.getToken())) {
            log.error("Request url：{}，method：{}，userId：{}，token：{}，拦截此请求：003-redis中userId对应redis中用户信息的token，与前端传入token，不一致！", url, method, userId, token);
            return false;
        }
        userService.userRedisInfoSave(RedisKeyUtil.getRedisUserInfoKey(userId), userRedis);
        request.setAttribute(SystemConstant.SYSTEM_TOKEN_NAME, token);
        request.setAttribute(SystemConstant.SYSTEM_USER_ID, userId);
        valueHolder.setTokenHolder(token);
        valueHolder.setUserIdHolder(Long.valueOf(userId));
        return true;
    }
}
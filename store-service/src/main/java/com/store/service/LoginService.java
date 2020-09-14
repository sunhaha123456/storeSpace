package com.store.service;

import com.store.data.to.request.UserLoginRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 功能：登录 service
 * @author sunpeng
 * @date 2018
 */
public interface LoginService {
    /**
     * 功能：登录
     * @param param
     * @return
     * @throws Exception
     */
    Map<String, Object> verify(UserLoginRequest param) throws Exception;

    /**
     * 功能：登出
     * @throws Exception
     */
    void out() throws Exception;

    /**
     * 功能：token校验，若通过校验，并更新token有效时长
     * @return true：通过校验，false：未通过校验
     */
    boolean tokenValidate(HttpServletRequest request);
}
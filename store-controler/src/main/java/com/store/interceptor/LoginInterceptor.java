package com.store.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.store.common.data.response.ResponseResult;
import com.store.common.data.response.ResponseResultCode;
import com.store.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 功能：登录拦截器
 * @author sunpeng
 * @date 2018
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Inject
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (!loginService.tokenValidate(request)) {
            String requestUrl = request.getRequestURI().toLowerCase();
            int failMsgType = requestUrl.lastIndexOf("/to");
            getFailMsg(response, failMsgType);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    // 设置返回的失败信息
    private void getFailMsg(HttpServletResponse response, int msgType) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            String msg = null;
            if (msgType > 0) {
                response.setContentType("text/html; charset=utf-8");
                msg = "<html><head><script>alert('登录信息已失效，请重新登录！');</script></head><body></body></html>";
            } else {
                response.setContentType("application/json; charset=utf-8");
                msg = JSONObject.toJSONString(ResponseResult.build(ResponseResultCode.LOGIN_FIRST));
            }
            out = response.getWriter();
            out.append(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
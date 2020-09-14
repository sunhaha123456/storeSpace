package com.store.aspect;

import com.store.common.util.JsonUtil;
import com.store.common.util.ValueHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 功能：日志
 * @author sunpeng
 * @date 2017
 */
@Slf4j
@Aspect
@Configuration
public class SysLogAspect {

    @Inject
    private ValueHolder valueHolder;

    private long startTime = 0; // 开始时间

    private final List<String> noLogList = Arrays.asList(
//        "com.szhl.sxtx.web.controller.mobile.OrderMobileController.changeOrderStatus",
    );

    @Pointcut("execution (* com.store.controler..*.*(..))")
    private void aspectMethod() {

    }

    @Before(value = "aspectMethod()")
    public void before(JoinPoint point) throws Exception {
        startTime = System.currentTimeMillis();   //获取开始时间
        String method = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
        if (!noLogList.contains(method)) {
            List param = null;
            if (point.getArgs() != null && point.getArgs().length > 0) {
                param = new ArrayList(point.getArgs().length);
                for (Object p : point.getArgs()) {
                    if (p != null && !(p instanceof HttpServletRequest) && !(p instanceof HttpServletResponse) && !(p instanceof BindingResult) && !(p instanceof MultipartFile)) {
                        param.add(p);
                    }
                }
            }
            log.info("日志：请求---userId：{}---method：{}---param：{}", valueHolder.getUserIdHolder(), method, JsonUtil.objectToJson(param));
            /*
            Long userId = valueHolder.getUserIdHolder();
            if (userId == null) {
                log.info("日志：请求---用户未登录---method：{}---param：{}", method, JsonUtil.objectToJson(param));
            } else {
                log.info(MarkerFactory.getMarker("USER_MARKER"), "日志：请求---用户已登录---userId：{}---method：{}---param：{}", userId, method, JsonUtil.objectToJson(param));
            }
            */
        }
    }

    @AfterReturning(value = "aspectMethod()", returning = "returnValue")
    public void after(JoinPoint point, Object returnValue) {
        String method = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
        if (!noLogList.contains(method)) {
            if (method.contains("exportFile")) {
                returnValue = null;
            }
            String returnStr = JsonUtil.objectToJson(returnValue);
            if (returnStr != null && returnStr.length() > 200) {
                returnStr = returnValue.toString();
            }
            log.info("日志：返回---userId：{}---method：{}---return：{}，共耗时-{}-毫秒", valueHolder.getUserIdHolder(), method, returnStr, System.currentTimeMillis() - startTime);
            /*
            Long userId = valueHolder.getUserIdHolder();
            if (userId == null) {
                log.info("日志：返回---用户未登录---method：{}---return：{}，共耗时-{}-毫秒", method, JsonUtil.objectToJson(returnValue), time);
            } else {
                log.info(MarkerFactory.getMarker("USER_MARKER"), "日志：返回---用户已登录---userId：{}---method：{}---return：{}，共耗时-{}-毫秒", userId, method, JsonUtil.objectToJson(returnValue), time);
            }
            */
        }
    }
}
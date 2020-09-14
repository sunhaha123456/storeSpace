package com.store.aspect;

import com.store.common.util.IpUtil;
import com.store.common.util.JsonUtil;
import com.store.common.util.ValueHolder;
import com.store.data.entity.TbSysUserLog;
import com.store.repository.SysUserLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 功能：用户操作行为的日志记录切面
 */
@Aspect
@Configuration
@Slf4j
public class UserOperateLogAspect {

    @Inject
    private SysUserLogRepository sysUserLogRepository;

    @Inject
    private ValueHolder valueHolder;

    @Pointcut("execution (* com.store.controler..*.*(..))")
    private void aspectMethod() {

    }

    @Pointcut("execution (* com.store.controler..list*(..))")
    private void listMethod() {

    }

    @Pointcut("execution (* com.store.controler..query*(..))")
    private void queryMethod() {

    }

    @Pointcut("execution (* com.store.controler..search*(..))")
    private void searchMethod() {

    }

    @Pointcut("execution (* com.store.controler..get*(..))")
    private void getMethod() {

    }

    @Pointcut("execution (* com.store.controler..count*(..))")
    private void countMethod() {

    }

    @Pointcut("execution (* com.store.controler..check*(..))")
    private void checkMethod() {

    }

    @Pointcut("execution (* com.store.controler..export*(..))")
    private void exportMethod() {

    }

    @Pointcut("execution (* com.store.controler..to*(..))")
    private void toMethod() {

    }

    @AfterReturning(value = "aspectMethod() && !listMethod() && !queryMethod() && !searchMethod() && !getMethod() && !countMethod() && !checkMethod() && !exportMethod() && !toMethod()", returning = "returnValue")
    public void after(JoinPoint point, Object returnValue) {
        try {
                Object[] args = point.getArgs();
                List<Object> objects = Arrays.asList(args);
                List<Object> newList = new ArrayList<>();
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                String url = request.getRequestURI();
                String ip = IpUtil.getIp(request);
                Object p = null;
                for (int i = 0; i < objects.size(); i++) {
                    p = objects.get(i);
                    if (p == null || p instanceof HttpServletRequest || p instanceof HttpServletResponse || p instanceof BindingResult || p instanceof MultipartFile) {
                        continue;
                    }
                    newList.add(objects.get(i));
                }
                //记录操作日志
                Long userIdHolder = valueHolder.getUserIdHolder();
                if (userIdHolder != null) {
                    TbSysUserLog sysUserLog = new TbSysUserLog(userIdHolder, url, ip, JsonUtil.objectToJson(newList), returnValue + "");
                    sysUserLog.setCreateDate(new Date());
                    sysUserLogRepository.save(sysUserLog);
                }
        } catch (Throwable e) {
            log.error("Throwable 日志记录失败！", e);
        }
    }
}
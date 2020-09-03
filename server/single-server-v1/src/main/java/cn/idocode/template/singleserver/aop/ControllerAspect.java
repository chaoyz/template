package cn.idocode.template.singleserver.aop;

import cn.idocode.template.singleserver.exception.ServiceException;
import cn.idocode.template.singleserver.model.ResultCode;
import cn.idocode.template.singleserver.model.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 通用的controller层切面处理
 *
 * @author levicyang
 * 2020-07-09 14:33
 */
@Slf4j
@Aspect
@Component
public class ControllerAspect implements Ordered {

    @Pointcut(value = "execution(public * cn.idocode.template.singleserver.controller..*(..)) && !@annotation(cn.idocode.template.singleserver.annotation.NotAspect)")
    public void controller() {
    }

    @Around("controller()")
    public Object aroundController(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();
        boolean accessLog = true;
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String requestUrl = null;
        String requestMethod = null;
        String remoteIp = null;
        String requestTraceId = null;
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpServletResponse response = servletRequestAttributes.getResponse();
            // TODO add user login info
            requestUrl = request.getRequestURI();
            requestMethod = request.getMethod();
            remoteIp = request.getRemoteAddr();
            requestTraceId = request.getHeader("X-Request-Id");
            if (StringUtils.isEmpty(requestTraceId)) {
                requestTraceId = UUID.randomUUID().toString();
            }
            log.info("requestTraceId:{}, clientIp:{}, requestUrl:{}, requestMethod:{}, loginInfo:{}",
                    requestTraceId, remoteIp, requestUrl, requestMethod, "TODO print login name");
        } else {
            accessLog = false;
        }
        Object resultMsg = null;
        try {
            resultMsg = pjp.proceed();
        } catch (Throwable throwable) {
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            Class<?> returnType = methodSignature.getReturnType();
            if (throwable instanceof ServiceException) {
                ServiceException e = (ServiceException) throwable;
                log.error("ControllerAspect aroundController exec controller service exception. requestTraceId:" + requestTraceId + " ServiceException:" + e, e);
                resultMsg = new ResultMsg<>(e.getCode(), e.getMsg());
            } else {
                log.error("ControllerAspect aroundController exec controller error. requestTraceId:" + requestTraceId, throwable);
                resultMsg = new ResultMsg<>(ResultCode.SERVER_ERROR.getCode(), "服务器内部错误，请联系管理员，异常信息:"
                        + throwable.getLocalizedMessage());
            }
        }
        long endTime = System.currentTimeMillis();
        if (accessLog) {
            log.info("requestTraceId:{}, clientIp:{}, requestUrl:{}, requestMethod:{}, handleTime:{}ms", requestTraceId,
                    remoteIp, requestUrl, requestMethod, (endTime - startTime));
        }
        return resultMsg;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

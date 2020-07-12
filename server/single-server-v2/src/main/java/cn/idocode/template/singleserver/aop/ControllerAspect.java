package cn.idocode.template.singleserver.aop;

import cn.idocode.template.singleserver.exception.ServiceException;
import cn.idocode.template.singleserver.model.ResultCode;
import cn.idocode.template.singleserver.model.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

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

    @Pointcut(value = "execution(public * cn.idocode.template.singleserver.controller..*(..))")
    public void controller() {
    }

    @Around("controller()")
    public Object aroundController(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String requestUrl = null;
        String requestMethod = null;
        String remoteIp = null;
        if (requestAttributes != null) {
            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            if (request != null) {
                requestUrl = request.getRequestURI();
                requestMethod = request.getMethod();
                remoteIp = request.getRemoteAddr();
            }
        }
        log.info("clientIp:{}, requestUrl:{}, requestMethod:{}", remoteIp, requestUrl, requestMethod);

        Object resultMsg;
        try {
            resultMsg = pjp.proceed();
        } catch (Throwable throwable) {
            if (throwable instanceof ServiceException) {
                ServiceException e = (ServiceException) throwable;
                resultMsg = new ResultMsg<>(e.getCode(), e.getMsg());
            } else {
                resultMsg = new ResultMsg<>(ResultCode.SERVER_ERROR.getCode(), "服务器内部错误，请联系管理员，异常信息:" + throwable.getLocalizedMessage());
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("clientIp:{}, requestUrl:{}, requestMethod:{}, handleTime:{}ms", remoteIp, requestUrl, requestMethod, (endTime - startTime));
        return resultMsg;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

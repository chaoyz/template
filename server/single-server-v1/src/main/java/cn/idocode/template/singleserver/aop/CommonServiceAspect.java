package cn.idocode.template.singleserver.aop;

import cn.idocode.template.singleserver.config.ProjectConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通用service 切面处理类
 *
 * @author levicyang
 * 2020-07-09 16:50
 */
@Slf4j
@Aspect
@Component
public class CommonServiceAspect {
    private final ObjectMapper om = new ObjectMapper();

    @Autowired
    private ProjectConfig projectConfig;

    @Pointcut(value = "execution(public * cn.idocode.template.singleserver.service..*(..)) && !@annotation(cn.idocode.template.singleserver.annotation.NotAspect)")
    public void servicePointcut() {
    }

    @Around(value = "servicePointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        // TODO 后续改进根据远程配置文件控制一些debug日志输出
        if (projectConfig.isDebugLog()) {
            Signature signature = pjp.getSignature();
            Class declaringType = signature.getDeclaringType();
            String typeName = signature.getDeclaringTypeName();
            String name = signature.getName();
            Object[] args = pjp.getArgs();
            String argsStr = null;
            try {
                argsStr = om.writeValueAsString(args);
            } catch (JsonProcessingException e) {
                log.warn("CommonServiceAspect json to string error, can ignore it.");
            }
            long startTime = System.currentTimeMillis();
            log.info("service package method invoke. declaringType:{}, DeclaringTypeName:{}, Name:{}, args:{}",
                    declaringType, typeName, name, argsStr);
            result = pjp.proceed();
            long endTime = System.currentTimeMillis();
            log.info("service package method invoke end. declaringType:{}, DeclaringTypeName:{}, Name:{}, args:{},time:{}ms",
                    declaringType, typeName, name, argsStr, (endTime - startTime));
        } else {
            result = pjp.proceed();
        }
        return result;
    }
}

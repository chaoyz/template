package cn.idocode.template.singleserver.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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

    @Pointcut(value = "execution(public * cn.idocode.template.singleserver.service..*(..))")
    public void servicePointcut() {}

    @Before("servicePointcut()")
    public void before() {
        // something log or other
    }

    @After("servicePointcut()")
    public void after() {
        // something log or other
    }
}

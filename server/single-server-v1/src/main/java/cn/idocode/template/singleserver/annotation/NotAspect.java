package cn.idocode.template.singleserver.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 用于标注是调用taf服务，mockClass标注启用mock状态时候指定使用的mock类
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface NotAspect {
}

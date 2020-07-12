package cn.idocode.template.singleserver.controller;

import cn.idocode.template.singleserver.model.ResultCode;
import cn.idocode.template.singleserver.model.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * controller异常处理类
 *
 * @author levicyang
 * 2020-07-09 14:17
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(value = Exception.class)
    Object handlerException(Exception e, HttpServletRequest request) {
        log.error("logic error. request url:" + request.getRequestURL(), e);
        ResultMsg<String> result = new ResultMsg<>();
        result.setCode(ResultCode.SERVER_ERROR.getCode());
        result.setMsg("服务器内部异常，请联系管理员，异常信息:" + e.getMessage());
        return result;
    }
}

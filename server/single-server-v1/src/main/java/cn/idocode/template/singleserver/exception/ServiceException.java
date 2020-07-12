package cn.idocode.template.singleserver.exception;

import cn.idocode.template.singleserver.model.ResultCode;
import lombok.Data;

@Data
public class ServiceException extends Exception {
    private Integer code;
    private String msg;

    public ServiceException() {
    }

    public ServiceException(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.name();
    }

    public ServiceException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

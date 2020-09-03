package cn.idocode.template.singleserver.exception;

import cn.idocode.template.singleserver.model.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends Exception {
    private Integer code;
    private String resultCode;
    private String msg;

    public ServiceException() {
    }

    public ServiceException(ResultCode resultCode) {
        super(resultCode.name());
        this.code = resultCode.getCode();
        this.resultCode = resultCode.name();
        this.msg = resultCode.getMsg();
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}

package cn.idocode.template.singleserver.model;

public enum ResultCode {
    // 预留http状态码
    OK(200),
    NOT_FOUND(404),
    SERVER_ERROR(500),

    // 自定义状态码
    SUCCESS(0),

    ;
    private final Integer code;

    ResultCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "code=" + code +
                '}';
    }
}

package cn.idocode.template.singleserver.model;

public enum ResultCode {
    // 预留http状态码
    OK(200, "OK"),
    NOT_FOUND(404, "NOT_FOUND"),
    SERVER_ERROR(500, "SERVER_ERROR"),

    // 自定义状态码 0 和 200 都为成功状态
    SUCCESS(0, "SUCCESS"),

    /********************************************************* 通用状态码 [1000, 2000) *********************************************************
     /**
     * 请求参数不合法
     */
    PARAM_ERROR(1000, "请求参数不合法"),

    /**
     * 操作zip压缩包解压失败
     */
    UNZIP_FILE_ERROR(1001, "解压ZIP压缩包失败"),

    ;
    private final Integer code;
    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}

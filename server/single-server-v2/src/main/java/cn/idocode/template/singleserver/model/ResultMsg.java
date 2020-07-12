package cn.idocode.template.singleserver.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultMsg<T> {
    private Integer code = 0;
    private String msg = "";
    private T data;

    public ResultMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

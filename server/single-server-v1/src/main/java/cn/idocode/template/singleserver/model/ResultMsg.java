package cn.idocode.template.singleserver.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResultMsg<T> {
    @ApiModelProperty("返回状态码")
    private Integer code = 0;
    @ApiModelProperty("返回信息描述")
    private String msg = "";
    @ApiModelProperty("接口返回数据")
    private T data;

    public ResultMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

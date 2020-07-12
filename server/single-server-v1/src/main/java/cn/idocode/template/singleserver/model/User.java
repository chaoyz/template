package cn.idocode.template.singleserver.model;

import lombok.Data;

@Data
public class User {
    private Long uid;
    private String name;
    private Integer age;
    private String tel;
    private String address;
}

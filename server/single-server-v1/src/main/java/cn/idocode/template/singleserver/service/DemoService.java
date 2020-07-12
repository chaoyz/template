package cn.idocode.template.singleserver.service;


import cn.idocode.template.singleserver.model.User;

public interface DemoService {

    String echo(String param) throws Exception;

    User getUser(String name);

    User saveUser(User user);
}

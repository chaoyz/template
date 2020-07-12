package cn.idocode.template.singleserver.service;


import cn.idocode.template.singleserver.model.User;

public interface DemoService {

    String echo(String param) throws Exception;

    User getUser(String name);

    User getUserWithNativeSqlQuery(String name);

    User getUserWithEntityManager(String name);

    User getUserWithPageQuery(String name, int page, int pageSize);

    User saveUser(User user);

    User saveUserWithNativeSqlInsert(User user);
}

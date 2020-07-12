package cn.idocode.template.singleserver.service.impl;

import cn.idocode.template.singleserver.model.User;
import cn.idocode.template.singleserver.repository.UserMapper;
import cn.idocode.template.singleserver.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 封装基本查询、插入操作的demo
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String echo(String param) throws Exception {
        return param;
    }

    @Override
    public User getUser(String name) {
        return userMapper.findUserByName(name);
    }

    @Override
    public User saveUser(User user) {
        userMapper.insertUser(user);
        return user;
    }
}

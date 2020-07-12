package cn.idocode.template.singleserver.service.impl;

import cn.idocode.template.singleserver.model.User;
import cn.idocode.template.singleserver.repository.repo1.UserRepository;
import cn.idocode.template.singleserver.repository.repo2.ReadUserRepository;
import cn.idocode.template.singleserver.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * 封装基本查询、插入操作的demo
 */
@Slf4j
@Service
public class DemoServiceImpl implements DemoService {

    private UserRepository userRepository;
    private ReadUserRepository readUserRepository;
    private LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBeanRead;

    public DemoServiceImpl(UserRepository userRepository, ReadUserRepository readUserRepository, @Qualifier("localContainerEntityManagerFactoryBeanRead") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBeanRead) {
        this.userRepository = userRepository;
        this.readUserRepository = readUserRepository;
        this.localContainerEntityManagerFactoryBeanRead = localContainerEntityManagerFactoryBeanRead;
    }

    @Override
    public String echo(String param) throws Exception {
        return param;
    }

    @Override
    public User getUser(String name) {
        return readUserRepository.findByName(name);
    }

    @Override
    public User getUserWithNativeSqlQuery(String name) {
        return readUserRepository.customQuerySqlFindUser(name);
    }

    @Override
    public User getUserWithEntityManager(String name) {
        log.info("getUserWithEntityManager start. name:{}", name);
        // 注意这里需要放置sql注入！！！！
        String sql = "select * from user where name = '" + name + "'";
        EntityManager em = localContainerEntityManagerFactoryBeanRead.getNativeEntityManagerFactory().createEntityManager();
        try {
            Query query = em.createNativeQuery(sql, User.class);
            List<User> user = query.getResultList();
            return user.size() > 0 ? user.get(0) : null;
        } finally {
            em.close();
        }
    }

    @Override
    public User getUserWithPageQuery(String name, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "uid");
        Page<User> users = this.readUserRepository.findByNameOrderByUidDesc(name, pageRequest);
        log.info("getUserWithPageQuery result:{}", users);
        return users.getContent().size() > 0 ? users.getContent().get(0) : null;
    }

    @Override
    public User saveUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User saveUserWithNativeSqlInsert(User user) {
        return null;
    }
}

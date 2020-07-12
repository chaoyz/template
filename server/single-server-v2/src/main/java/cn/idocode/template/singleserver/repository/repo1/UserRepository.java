package cn.idocode.template.singleserver.repository.repo1;

import cn.idocode.template.singleserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levic
 * 2020/7/12 6:59 PM
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 注意自定义sql的时候增加事务注解
     * 处了查询外，insert delete update 增加注解Modifying
     */
    @Modifying
    @Transactional
    @Query(value = "insert into user (`name`, `age`, `tel`, `address`) values (:user.name, :user.age, :user.tel, :user.address)", nativeQuery = true)
    int insertUser(@Param("user") User user);
}

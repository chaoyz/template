package cn.idocode.template.singleserver.repository.repo2;

import cn.idocode.template.singleserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author levic
 * 2020/7/12 8:09 PM
 **/
@Repository
public interface ReadUserRepository extends JpaRepository<User, Long> {
    User findByName(String name);

    @Query(value = "select * from user where name=:name", nativeQuery = true)
    User customQuerySqlFindUser(@Param("name") String name);

    Page<User> findByNameOrderByUidDesc(String name, Pageable pageable);
}

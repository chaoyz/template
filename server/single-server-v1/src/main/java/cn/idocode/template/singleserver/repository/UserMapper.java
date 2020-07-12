package cn.idocode.template.singleserver.repository;

import cn.idocode.template.singleserver.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    @Select("select * from user where name = #{name}")
    User findUserByName(@Param("name") String name);

    @Insert("insert into `user` (`name`, `age`, `tel`, `address`) VALUES (#{user.name}, #{user.age}, #{user.tel}, #{user.address})")
    @SelectKey(statement = "select LAST_INSERT_ID();", keyProperty = "user.uid", keyColumn = "uid", before = false, resultType = Long.class)
    int insertUser(@Param("user") User user);
}

package cn.idocode.template.singleserver.repository.read;

import cn.idocode.template.singleserver.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserReadMapper {
    @Select("select * from user where name = #{name}")
    User findUserByName(@Param("name") String name);
}

package cn.idocode.template.singleserver.repository.write;

import cn.idocode.template.singleserver.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserWriteMapper {
    @Insert("insert into `user` (`name`, `age`, `tel`, `address`) VALUES (#{user.name}, #{user.age}, #{user.tel}, #{user.address})")
    @SelectKey(statement = "select LAST_INSERT_ID();", keyProperty = "user.uid", keyColumn = "uid", before = false, resultType = Long.class)
    int insertUser(@Param("user") User user);
}

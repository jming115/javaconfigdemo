package com.mfun.javaconfigdemo.dao;

import com.mfun.javaconfigdemo.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @Auther: JMing
 * @Date: 2019/11/26 16:53
 * @Description:
 */

@Repository
public interface UserDao {
    @Results(id = "UserDao", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "sex", property = "sex"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "modified_time", property = "modifiedTime")
    })
    @Select("SELECT * FROM tb_user WHERE id=#{arg0}")
    User getUserById(Long id);

    @Insert("INSERT INTO tb_user(name, sex) VALUES(#{name}, #{sex})")
    @Options(useGeneratedKeys = true)
    int add(User userInfo);

    @Update("UPDATE tb_user SET name=#{arg1.name}, sex=#{arg1.sex}, modified_time=now() WHERE id=#{arg0}")
    int updateById(Long id, User user);
}

package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from sky_take_out.user where openid = #{openId}")
    User getUserByOpenId(String openId);

    @Select("select  * from sky_take_out.user where  id = #{id}")
    User getById(long id);
    void insert(User user);
}

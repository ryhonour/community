package com.ry.community.mapper;

import com.ry.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 10:53 2019/8/17
 * @Version 1.0
 */
@Mapper
@Component("userMapper")
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) " +
            "values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}

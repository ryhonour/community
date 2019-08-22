package com.ry.community.service;

import com.ry.community.mapper.UserMapper;
import com.ry.community.model.User;
import com.ry.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 11:14 2019/8/21
 * @Version 1.0
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User getUserByToken(String token) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andTokenEqualTo(token);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList.size() != 0) {
            return userList.get(0);
        }
        return null;
    }

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList.size() == 0) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新
            User dbUser = userList.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            updateUser.setBio(user.getBio());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }
}

package com.ry.community.model;

import lombok.Data;

/**
 * @Author: rongyao
 * @Description: @Data注解自动生成get、set方法
 * @Date: Create in 11:15 2019/8/17
 * @Version 1.0
 */
@Data
public class User {
    private int id;
    private String name;
    private String accountId;
    //token用来标记该账号是否在Cookie中存在，用于页面持久化登录的判断，是一串UUID自动生成的字符串
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}

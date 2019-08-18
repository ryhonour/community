package com.ry.community.dto;

import lombok.Data;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 14:53 2019/8/15
 * @Version 1.0
 */
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}

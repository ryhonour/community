package com.ry.community.dto;

import lombok.Data;

/**
 * @Author: rongyao
 * @Description: github登录过程中的access_token所需要的参数
 * @Date: Create in 11:28 2019/8/15
 * @Version 1.0
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}

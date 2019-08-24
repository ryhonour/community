package com.ry.community.exception;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 10:47 2019/8/24
 * @Version 1.0
 */
public enum CustomizeErrorCodeImpl implements CustomizeErrorCode {
    /**
     * 异常Message的枚举
     */
    QUESTION_NOT_FIND("您找的问题不存在或已被删除", 2001),
    CLIENT_ERROR("请求有错，要不换个姿势？", 2002),
    SERVER_ERROR("服务器冒烟了，请稍后再试", 2003);


    private String message;
    private Integer code;

    CustomizeErrorCodeImpl(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

}

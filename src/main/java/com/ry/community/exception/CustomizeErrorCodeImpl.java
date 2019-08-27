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
    QUESTION_NOT_FIND(2001, "您找的问题不存在或已被删除"),
    USER_NOT_FIND(2002, "当前操作未登录，请先登录！"),
    TARGET_PARAM_NOT_FIND(2003, "未选中任何问题或回复"),
    SYS_ERROR(2004, "服务器太热了，请稍后再访问..."),
    TYPE_TARGET_ERROR(2005, "评论类型不存在或错误"),
    REPLY_COMMENT_NOT_FIND(2006, "您回复的评论不存在"),
    REPLY_QUESTION_NOT_FIND(2006, "您回复的问题不存在");


    private String message;
    private Integer code;

    CustomizeErrorCodeImpl(Integer code, String message) {
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

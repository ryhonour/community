package com.ry.community.exception;

/**
 * @author rongyao
 * @date Create in 12:13 2019/10/24
 */
public enum CustomizeSucceedCodeImpl implements CustomizeCode {
    /***
     *  成功Message的枚举
     */
    PUBLISHED_SUCCESSFULLY(201, "问题发表成功！");

    private Integer code;
    private String message;

    CustomizeSucceedCodeImpl(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

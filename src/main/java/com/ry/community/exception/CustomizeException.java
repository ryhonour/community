package com.ry.community.exception;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 10:43 2019/8/24
 * @Version 1.0
 */
public class CustomizeException extends RuntimeException {
    private String message;
    private Integer code;

    public CustomizeException(CustomizeErrorCode customizeErrorCode) {
        this.message = customizeErrorCode.getMessage();
        this.code = customizeErrorCode.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

}
